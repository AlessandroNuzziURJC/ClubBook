package clubbook.backend.controller;

import clubbook.backend.dtos.EventDto;
import clubbook.backend.dtos.NewEventDto;
import clubbook.backend.model.Event;
import clubbook.backend.model.EventType;
import clubbook.backend.responses.ResponseMessages;
import clubbook.backend.responses.ResponseWrapper;
import clubbook.backend.service.EventService;
import clubbook.backend.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller to manage events.
 * Provides endpoints for creating, editing, retrieving, and deleting events,
 * as well as generating PDF reports for events.
 */
@Validated
@RequestMapping("/event")
@RestController
public class EventController {

    private final EventService eventService;
    private final SeasonService seasonService;

    @Autowired
    public EventController(EventService eventService, SeasonService seasonService) {
        this.eventService = eventService;
        this.seasonService = seasonService;
    }

    /**
     * Retrieves the list of event types.
     *
     * @return A response entity containing the list of event types.
     */
    @GetMapping("/types")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<List<EventType>> getEventTypes() {
        return ResponseEntity.ok(this.eventService.getEventTypes());
    }

    /**
     * Saves a new event based on the provided event data.
     *
     * @param newEventDto The data transfer object containing the new event information.
     * @return A response indicating whether the new event was successfully registered.
     */
    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<ResponseWrapper<Boolean>> saveNewEvent(@RequestBody NewEventDto newEventDto) {
        boolean output = this.eventService.save(newEventDto);
        if (!output)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.NEW_EVENT_REGISTERED_CORRECT, output));
    }

    /**
     * Edits an existing event based on the provided event data.
     *
     * @param editEventDto The data transfer object containing the edited event information.
     * @return A response indicating whether the event was successfully edited.
     */
    @PutMapping("/edit")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<ResponseWrapper<Boolean>> editEvent(@RequestBody EventDto editEventDto) {
        boolean output = this.eventService.saveEdited(editEventDto);
        if (!output)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.EDIT_EVENT_REGISTERED_CORRECT, output));
    }

    /**
     * Retrieves all future events for a specific user based on their role.
     *
     * @param userId The ID of the user for whom to retrieve events.
     * @return A response containing the list of future events.
     */
    @GetMapping("/all/{userId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'TEACHER', 'STUDENT')")
    public ResponseEntity<ResponseWrapper<List<EventDto>>> getAllEvents(@PathVariable int userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream()
                .findFirst().orElseThrow()
                .getAuthority();

        List<EventDto> events;

        if (role.equals("ROLE_ADMINISTRATOR")) {
            events = this.eventService.findAllFutureEvents();
        } else if (role.equals("ROLE_TEACHER")) {
            if (!this.seasonService.seasonStarted()) {
                return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
            }
            events = this.eventService.findAllFutureEvents();
        } else {
            if (!this.seasonService.seasonStarted()) {
                return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
            }

            events = this.eventService.findStudentFutureEvents(userId);
        }

        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, events));
    }

    /**
     * Retrieves events for a specific month and year for a user based on their role.
     *
     * @param monthValue The month for which to retrieve events.
     * @param year      The year for which to retrieve events.
     * @param userId    The ID of the user for whom to retrieve events.
     * @return A response containing a map of days to events for the specified month.
     */
    @GetMapping("/month/{monthValue}/{year}/{userId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'TEACHER', 'STUDENT')")
    public ResponseEntity<ResponseWrapper<Map<Integer,List<EventDto>>>> getMonthEvents(@PathVariable int monthValue, @PathVariable int year, @PathVariable int userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream()
                .findFirst().orElseThrow()
                .getAuthority();

        Map<Integer, List<EventDto>> events;

        if (role.equals("ROLE_ADMINISTRATOR")) {
            events = this.eventService.findActualMonthEvents(monthValue, year);
        } else if (role.equals("ROLE_TEACHER")) {
            if (!this.seasonService.seasonStarted()) {
                return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
            }
            events = this.eventService.findActualMonthEvents(monthValue, year);
        } else {
            if (!this.seasonService.seasonStarted()) {
                return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
            }
            events = this.eventService.findActualMonthEventsStudent(monthValue, year, userId);
        }
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, events));
    }

    /**
     * Retrieves all past events.
     *
     * @return A response containing the list of past events.
     */
    @GetMapping("/past")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'TEACHER')")
    public ResponseEntity<ResponseWrapper<List<EventDto>>> getAllPastEvents() {
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, this.eventService.findAllPastEvents()));
    }

    /**
     * Retrieves the next event for a specific user based on their role.
     *
     * @param userId The ID of the user for whom to retrieve the next event.
     * @return A response containing the next event.
     */
    @GetMapping("/next/{userId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'TEACHER', 'STUDENT')")
    public ResponseEntity<ResponseWrapper<EventDto>> getNextEvent(@PathVariable int userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream()
                .findFirst().orElseThrow()
                .getAuthority();

        EventDto eventDto;
        if (role.equals("ROLE_ADMINISTRATOR")) {
            eventDto = this.eventService.findNextEvent();
        } else if (role.equals("ROLE_TEACHER")) {
            if (!this.seasonService.seasonStarted()) {
                return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
            }
            eventDto = this.eventService.findNextEvent();
        } else {
            if (!this.seasonService.seasonStarted()) {
                return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
            }
            eventDto = this.eventService.findNextEventStudent(userId);
        }

        if (eventDto == null)
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.NO_FUTURE_EVENTS, null));
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, eventDto));
    }

    /**
     * Deletes an event with the specified ID.
     *
     * @param eventId The ID of the event to be deleted.
     * @return A response indicating whether the event was successfully deleted.
     */
    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<ResponseWrapper<Boolean>> deleteEvent(@PathVariable Integer eventId) {
        this.eventService.deleteEvent(eventId);
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.EVENT_DELETED_SUCCESS, true));
    }

    /**
     * Generates a PDF report for the specified event.
     *
     * @param eventId The ID of the event for which to generate the PDF.
     * @return A response entity containing the generated PDF report.
     */
    @GetMapping("/generatepdf/{eventId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMINISTRATOR')")
    public ResponseEntity<byte[]> generatePdf(@PathVariable String eventId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream()
                .findFirst().orElseThrow()
                .getAuthority();

        byte[] output = new byte[0];
        if (role.equals("ROLE_ADMINISTRATOR")) {
            output = this.eventService.generatePdf(Integer.parseInt(eventId));
        } else if (role.equals("ROLE_TEACHER")) {
            if (!this.seasonService.seasonStarted()) {
                return ResponseEntity.badRequest().build();
            }
            output = this.eventService.generatePdf(Integer.parseInt(eventId));
        }

        

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        Event event = eventService.findEvent(Integer.parseInt(eventId));
        headers.setContentDispositionFormData("filename", "Attendance_Event_" + event.getTitle() + ".pdf");

        return ResponseEntity.ok().headers(headers).body(output);

    }
}
