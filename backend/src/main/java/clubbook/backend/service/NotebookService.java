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
import java.util.stream.Collectors;

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

    public Notebook getNotebook(int id) {
        return notebookRepository.findById(id).orElseThrow();
    }


    public List<Notebook> getAllNotebooks() {
        return notebookRepository.findAll();
    }


    public void deleteAllNotebooks() {
        this.notebookRepository.deleteAll();
    }

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

    public Page<NotebookEntry> getEntries(int notebookId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return this.notebookEntryRepository.findEntries(notebookId, pageable);
    }

    public NotebookEntry getNotebookEntryToday(Integer id) {
        return this.notebookEntryRepository.findTodayEntryByNotebookId(id);
    }

    public Boolean deleteEntry(Integer id) {
        this.notebookEntryRepository.deleteById(id);
        return true;
    }

    public NotebookEntry editNotebookEntry(NotebookEntry notebookEntry) {
        NotebookEntry update = this.notebookEntryRepository.findById(notebookEntry.getId()).orElseThrow();
        update.setWarmUpExercises(notebookEntry.getWarmUpExercises());
        update.setSpecificExercises(notebookEntry.getSpecificExercises());
        update.setFinalExercises(notebookEntry.getFinalExercises());
        this.notebookEntryRepository.save(update);
        return update;
    }

    public Set<LocalDate> getAllDates(int notebookId) {
        return this.notebookEntryRepository.findAllDates(notebookId);
    }

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
