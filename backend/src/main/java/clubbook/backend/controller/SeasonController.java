package clubbook.backend.controller;

import clubbook.backend.model.Season;
import clubbook.backend.service.AttendanceService;
import clubbook.backend.service.EventService;
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
    private final AttendanceService attendanceService;
    private final EventService eventService;

    @Autowired
    public SeasonController(SeasonService seasonService, AttendanceService attendanceService, EventService eventService) {
        this.seasonService = seasonService;
        this.attendanceService = attendanceService;
        this.eventService = eventService;
    }

    @GetMapping("/started")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<Season> hasStarted() {
        Season season = seasonService.seasonActive();
        if (season == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(season);
    }

    @PostMapping("/start/{adminId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<Boolean> startSeason(@PathVariable int adminId) {
        return ResponseEntity.ok(seasonService.startSeason(adminId));
    }

    @PostMapping("/finish/{adminId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<Boolean> finishSeason(@PathVariable int adminId) {
        boolean output = seasonService.finishSeason(adminId);
        this.attendanceService.deleteAll();
        this.eventService.deleteAll();
        return ResponseEntity.ok(output);
    }

}
