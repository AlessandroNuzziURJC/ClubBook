package clubbook.backend.service;

import clubbook.backend.model.ClassGroup;
import clubbook.backend.model.Season;
import clubbook.backend.repository.SeasonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class for managing the seasons within the application.
 * This class provides methods to start, finish, and check the status of a season.
 */
@Service
public class SeasonService {

    private final SeasonRepository seasonRepository;
    private final UserService userService;
    private final ClassGroupService classGroupService;
    private final NotebookService notebookService;

    /**
     * Constructs a new SeasonService with the specified dependencies.
     *
     * @param seasonRepository the repository for managing season data
     * @param userService the service for managing user-related operations
     * @param classGroupService the service for managing class groups
     * @param notebookService the service for managing notebooks
     */
    @Autowired
    public SeasonService(SeasonRepository seasonRepository, UserService userService, ClassGroupService classGroupService, NotebookService notebookService) {
        this.seasonRepository = seasonRepository;
        this.userService = userService;
        this.classGroupService = classGroupService;
        this.notebookService = notebookService;
    }

    /**
     * Checks if a season has already started.
     *
     * @return true if there is an active season, false otherwise
     */
    public boolean seasonStarted() {
        return this.seasonRepository.findByActive(true) != null;
    }

    /**
     * Retrieves the currently active season.
     *
     * @return the active Season if one exists, null otherwise
     */
    public Season seasonActive() {
        return this.seasonRepository.findByActive(true);
    }

    /**
     * Starts a new season and creates notebooks for all class groups.
     *
     * @param adminId the ID of the admin who is starting the season
     * @return true if the season was successfully started
     */
    public boolean startSeason(int adminId) {
        Season season = new Season();
        season.setInit(LocalDate.now());
        season.setActive(true);
        season.setAdminCreator(this.userService.findById(adminId));
        this.seasonRepository.save(season);

        List<ClassGroup> allClassGroups = this.classGroupService.getAllClassGroups();
        for (ClassGroup c : allClassGroups) {
            this.notebookService.createNotebook(c);
        }
        return true;
    }

    /**
     * Finishes the current active season, deactivating it and removing users.
     *
     * @param adminId the ID of the admin who is finishing the season
     * @return true if the season was successfully finished
     */
    @Transactional
    public boolean finishSeason(int adminId) {
        Season season = this.seasonRepository.findByActive(true);
        season.setActive(false);
        season.setAdminFinisher(this.userService.findById(adminId));
        season.setFinish(LocalDate.now());
        this.userService.removeUsers();
        this.seasonRepository.save(season);

        this.notebookService.deleteAllNotebooks();
        return true;
    }
}
