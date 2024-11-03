package clubbook.backend.controller;

import clubbook.backend.model.Season;
import clubbook.backend.service.AttendanceService;
import clubbook.backend.service.EventService;
import clubbook.backend.service.NotificationService;
import clubbook.backend.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * REST controller for managing season-related operations in the application.
 */

@RestController
@RequestMapping("/season")
public class SeasonController {

    private final SeasonService seasonService;
    private final AttendanceService attendanceService;
    private final EventService eventService;
    private final NotificationService notificationService;

    /**
     * Constructor for injecting necessary services into the SeasonController.
     *
     * @param seasonService       Service for handling season operations.
     * @param attendanceService   Service for managing attendance records.
     * @param eventService        Service for handling event operations.
     * @param notificationService Service for managing notifications.
     */
    @Autowired
    public SeasonController(SeasonService seasonService, AttendanceService attendanceService, EventService eventService, NotificationService notificationService) {
        this.seasonService = seasonService;
        this.attendanceService = attendanceService;
        this.eventService = eventService;
        this.notificationService = notificationService;
    }

    /**
     * Checks if there is an active season.
     *
     * @return The active season if it exists; HTTP 404 response otherwise.
     */
    @GetMapping("/started")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<Season> hasStarted() {
        Season season = seasonService.seasonActive();
        if (season == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(season);
    }

    /**
     * Starts a new season.
     *
     * @param adminId The ID of the administrator starting the season.
     * @return HTTP response with true if the season was successfully started; false otherwise.
     */
    @PostMapping("/start/{adminId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<Boolean> startSeason(@PathVariable int adminId) {
        return ResponseEntity.ok(seasonService.startSeason(adminId));
    }

    /**
     * Finishes the current season, removing all attendance records, events, and notifications.
     *
     * @param adminId The ID of the administrator finishing the season.
     * @return HTTP response with true if the season was successfully finished; false otherwise.
     */
    @PostMapping("/finish/{adminId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<Boolean> finishSeason(@PathVariable int adminId) {
        boolean output = seasonService.finishSeason(adminId);
        this.attendanceService.deleteAll();
        this.eventService.deleteAll();
        this.notificationService.deleteAll();
        return ResponseEntity.ok(output);
    }

}
