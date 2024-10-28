package clubbook.backend.service;

import clubbook.backend.model.ClassGroup;
import clubbook.backend.model.Season;
import clubbook.backend.repository.SeasonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SeasonService {

    private final SeasonRepository seasonRepository;
    private final UserService userService;
    private final ClassGroupService classGroupService;
    private final NotebookService notebookService;

    @Autowired
    public SeasonService(SeasonRepository seasonRepository, UserService userService, ClassGroupService classGroupService, NotebookService notebookService) {
        this.seasonRepository = seasonRepository;
        this.userService = userService;
        this.classGroupService = classGroupService;
        this.notebookService = notebookService;
    }

    public boolean seasonStarted() {
        return this.seasonRepository.findByActive(true) != null;
    }

    public Season seasonActive() {
        return this.seasonRepository.findByActive(true);
    }

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
