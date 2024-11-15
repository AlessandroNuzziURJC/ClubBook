package clubbook.backend.service;

import clubbook.backend.dtos.NotebookEntryDto;
import clubbook.backend.dtos.NotebookUpdateConfigDto;
import clubbook.backend.model.ClassGroup;
import clubbook.backend.model.Notebook;
import clubbook.backend.model.NotebookEntry;
import clubbook.backend.model.Schedule;
import clubbook.backend.repository.NotebookEntryRepository;
import clubbook.backend.repository.NotebookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Service class for managing notebooks and their entries.
 */
@Service
public class NotebookService {

    private final NotebookRepository notebookRepository;
    private final NotebookEntryRepository notebookEntryRepository;
    private final GPTService gptService;

    @Autowired
    public NotebookService (NotebookRepository notebookRepository, NotebookEntryRepository notebookEntryRepository, GPTService gptService) {
        this.notebookRepository = notebookRepository;
        this.notebookEntryRepository = notebookEntryRepository;
        this.gptService = gptService;
    }

    /**
     * Creates a new notebook for the specified class group.
     *
     * @param classGroup the class group for which to create the notebook
     */
    public void createNotebook(ClassGroup classGroup) {
        Notebook notebook = new Notebook();

        notebook.setSport(null);
        notebook.setLevel(null);
        notebook.setDuration(null);

        List<Integer> birthYears = classGroup.getStudents().stream()
                .map(student -> student.getBirthday().getYear())
                .toList();

        int minYear = birthYears.stream().min(Integer::compareTo).orElseThrow();
        int maxYear = birthYears.stream().max(Integer::compareTo).orElseThrow();
        String ageRange = minYear + " - " + maxYear;
        notebook.setAgeRange(ageRange);
        notebook.setClassgroup(classGroup);
        notebook.setEntries(new ArrayList<>());
        int agg = 0;
        for (Schedule schedule : classGroup.getSchedules()) {
            agg += schedule.getDuration();
        }

        notebook.setDuration(Integer.toString(agg/classGroup.getSchedules().size()));
        this.notebookRepository.save(notebook);
    }

    /**
     * Retrieves a notebook by its ID.
     *
     * @param id the ID of the notebook to retrieve
     * @return the notebook associated with the given ID
     */
    public Notebook getNotebook(int id) {
        return notebookRepository.findById(id).orElseThrow();
    }

    /**
     * Retrieves all notebooks.
     *
     * @return a list of all notebooks
     */
    public List<Notebook> getAllNotebooks() {
        return notebookRepository.findAll();
    }

    /**
     * Deletes all notebooks from the repository.
     */
    public void deleteAllNotebooks() {
        this.notebookRepository.deleteAll();
    }

    /**
     * Updates the configuration of a notebook.
     *
     * @param notebookUpdateConfigDto the DTO containing the updated notebook configuration
     * @return true if the update was successful, false otherwise
     */
    public boolean updateNotebookConfiguration(NotebookUpdateConfigDto notebookUpdateConfigDto) {
        Notebook notebook = notebookRepository.findById(notebookUpdateConfigDto.getId()).orElse(null);
        if (notebook == null) {
            return false;
        }
        notebook.setSport(notebookUpdateConfigDto.getSport());
        notebook.setLevel(notebookUpdateConfigDto.getLevel());
        this.notebookRepository.save(notebook);
        return true;
    }

    /**
     * Adds a new entry to the specified notebook.
     *
     * @param notebookEntryDto the DTO containing entry details
     * @param id              the ID of the notebook to which the entry will be added
     * @return the newly added notebook entry
     * @throws IllegalArgumentException if the date already exists in the notebook
     */
    public NotebookEntry addNotebookEntry(NotebookEntryDto notebookEntryDto, int id) {
        Notebook notebook = this.notebookRepository.findById(id).orElseThrow();
        NotebookEntry notebookEntry = new NotebookEntry();
        Set<LocalDate> allDates = this.getAllDates(id);
        if (allDates.contains(notebookEntryDto.getDate())) {
            throw new IllegalArgumentException("Date already exists");
        }
        notebookEntry.setDate(notebookEntryDto.getDate());
        notebookEntry.setNotebook(notebook);
        notebookEntry.setWarmUpExercises(notebookEntryDto.getWarmUpExercises());
        notebookEntry.setSpecificExercises(notebookEntryDto.getSpecificExercises());
        notebookEntry.setFinalExercises(notebookEntryDto.getFinalExercises());
        notebook.getEntries().add(notebookEntry);

        this.notebookRepository.save(notebook);
        return notebookEntry;
    }

    /**
     * Retrieves paginated entries of a notebook.
     *
     * @param notebookId the ID of the notebook
     * @param pageNumber the page number to retrieve
     * @param pageSize   the number of entries per page
     * @return a page of notebook entries
     */
    public Page<NotebookEntry> getEntries(int notebookId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return this.notebookEntryRepository.findEntries(notebookId, pageable);
    }

    /**
     * Retrieves the notebook entry for today.
     *
     * @param id the ID of the notebook
     * @return the notebook entry for today
     */
    public NotebookEntry getNotebookEntryToday(Integer id) {
        return this.notebookEntryRepository.findTodayEntryByNotebookId(id);
    }

    /**
     * Deletes a specific entry by its ID.
     *
     * @param id the ID of the entry to delete
     * @return true if the deletion was successful
     */
    public Boolean deleteEntry(Integer id) {
        this.notebookEntryRepository.deleteById(id);
        return true;
    }

    /**
     * Edits an existing notebook entry.
     *
     * @param notebookEntry the notebook entry with updated information
     * @return the updated notebook entry
     */
    public NotebookEntry editNotebookEntry(NotebookEntry notebookEntry) {
        NotebookEntry update = this.notebookEntryRepository.findById(notebookEntry.getId()).orElseThrow();
        update.setWarmUpExercises(notebookEntry.getWarmUpExercises());
        update.setSpecificExercises(notebookEntry.getSpecificExercises());
        update.setFinalExercises(notebookEntry.getFinalExercises());
        this.notebookEntryRepository.save(update);
        return update;
    }

    /**
     * Retrieves all dates for notebook entries.
     *
     * @param notebookId the ID of the notebook
     * @return a set of dates for the notebook entries
     */
    public Set<LocalDate> getAllDates(int notebookId) {
        return this.notebookEntryRepository.findAllDates(notebookId);
    }

    /**
     * Generates a notebook entry based on previous entries or the initial class information.
     *
     * @param id   the ID of the notebook
     * @param date the date for the new entry
     * @return a newly generated notebook entry
     * @throws Exception if there is an issue generating the entry
     */
    public NotebookEntry generateEntry(int id, LocalDate date) throws Exception {
        Notebook notebook = this.notebookRepository.findById(id).orElseThrow();
        List<NotebookEntry> list = this.notebookEntryRepository.findEntriesBeforeDate(date);
        StringBuilder prompt;
        String output;
        if (list.isEmpty()) {
            prompt = new StringBuilder("Deporte: " + notebook.getSport() + ". Edad: " + notebook.getAgeRange() + ". Nivel: " +
                    notebook.getLevel() + ". Duración: " + notebook.getDuration() + ". Primera clase.");
            output = gptService.generateResponse(prompt.toString());
        } else {
            prompt = new StringBuilder("Deporte: " + notebook.getSport() + ". Edad: " + notebook.getAgeRange() + ". Nivel: " +
                    notebook.getLevel() + ". Duración: " + notebook.getDuration() + "Clases anteriores: ");
            for (NotebookEntry entry : list) {
                prompt.append(entry.generateExercisesData());
            }
            output = gptService.generateResponse(prompt.toString());
        }

        return new NotebookEntry(output, date);
    }
}
