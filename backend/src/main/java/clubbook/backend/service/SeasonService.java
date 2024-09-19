package clubbook.backend.service;

import clubbook.backend.model.Season;
import clubbook.backend.repository.SeasonRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SeasonService {

    private final SeasonRepository seasonRepository;
    private final UserService userService;

    public SeasonService(SeasonRepository seasonRepository, UserService userService) {
        this.seasonRepository = seasonRepository;
        this.userService = userService;
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
        return true;
    }

    public boolean finishSeason(int adminId) {
        Season season = this.seasonRepository.findByActive(true);
        season.setActive(false);
        season.setAdminFinisher(this.userService.findById(adminId));
        season.setFinish(LocalDate.now());
        this.seasonRepository.save(season);
        return true;
    }
}
