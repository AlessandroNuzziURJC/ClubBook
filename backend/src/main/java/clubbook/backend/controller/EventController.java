package clubbook.backend.controller;

import clubbook.backend.dtos.EventDto;
import clubbook.backend.dtos.NewEventDto;
import clubbook.backend.model.EventAttendance;
import clubbook.backend.model.EventType;
import clubbook.backend.responses.ResponseMessages;
import clubbook.backend.responses.ResponseWrapper;
import clubbook.backend.service.EventAttendanceService;
import clubbook.backend.service.EventService;
import clubbook.backend.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/types")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<List<EventType>> getEventTypes() {
        return ResponseEntity.ok(this.eventService.getEventTypes());
    }

    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<ResponseWrapper<Boolean>> saveNewEvent(@RequestBody NewEventDto newEventDto) {
        boolean output = this.eventService.save(newEventDto);
        if (!output)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.NEW_EVENT_REGISTERED_CORRECT, output));
    }

    @PutMapping("/edit")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<ResponseWrapper<Boolean>> editEvent(@RequestBody EventDto editEventDto) {
        boolean output = this.eventService.saveEdited(editEventDto);
        if (!output)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.EDIT_EVENT_REGISTERED_CORRECT, output));
    }

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

    @GetMapping("/past")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'TEACHER')")
    public ResponseEntity<ResponseWrapper<List<EventDto>>> getAllPastEvents() {
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, this.eventService.findAllPastEvents()));
    }

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

    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<ResponseWrapper<Boolean>> deleteEvent(@PathVariable Integer eventId) {
        boolean output = this.eventService.deleteEvent(eventId);
        if (!output) {
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.EVENT_NOT_FOUND, false));
        }
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.EVENT_DELETED_SUCCESS, true));
    }
}
