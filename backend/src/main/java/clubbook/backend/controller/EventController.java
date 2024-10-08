package clubbook.backend.controller;

import clubbook.backend.dtos.EventDto;
import clubbook.backend.dtos.NewEventDto;
import clubbook.backend.model.EventAttendance;
import clubbook.backend.model.EventType;
import clubbook.backend.responses.ResponseMessages;
import clubbook.backend.responses.ResponseWrapper;
import clubbook.backend.service.EventAttendanceService;
import clubbook.backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Validated
@RequestMapping("/event")
@RestController
public class EventController {

    private final EventService eventService;


    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
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

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'TEACHER')")
    public ResponseEntity<ResponseWrapper<List<EventDto>>> getAllEvents() {
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, this.eventService.findAllFutureEvents()));
    }

    @GetMapping("/month/{monthValue}/{year}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'TEACHER')")
    public ResponseEntity<ResponseWrapper<Map<Integer,List<EventDto>>>> getMonthEvents(@PathVariable int monthValue, @PathVariable int year) {
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, this.eventService.findActualMonthEvents(monthValue, year)));
    }

    @GetMapping("/past")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'TEACHER')")
    public ResponseEntity<ResponseWrapper<List<EventDto>>> getAllPastEvents() {
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, this.eventService.findAllPastEvents()));
    }

    @GetMapping("/next")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'TEACHER')")
    public ResponseEntity<ResponseWrapper<EventDto>> getNextEvent() {
        EventDto eventDto = this.eventService.findNextEvent();
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
