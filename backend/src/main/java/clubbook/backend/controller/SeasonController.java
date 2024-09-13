package clubbook.backend.controller;

import clubbook.backend.model.Season;
import clubbook.backend.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/season")
public class SeasonController {

    private final SeasonService seasonService;

    @Autowired
    public SeasonController(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @PutMapping("/start/{adminId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<Boolean> startSeason(@PathVariable int adminId) {
        return ResponseEntity.ok(seasonService.startSeason(adminId));
    }

    @PutMapping("/finish/{adminId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<Boolean> finishSeason(@PathVariable int adminId) {
        return ResponseEntity.ok(seasonService.finishSeason(adminId));
    }

}
