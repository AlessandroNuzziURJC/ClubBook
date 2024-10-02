package clubbook.backend.controller;

import clubbook.backend.dtos.NewEventDto;
import clubbook.backend.model.EventType;
import clubbook.backend.model.EventTypeEnum;
import clubbook.backend.responses.ResponseMessages;
import clubbook.backend.responses.ResponseWrapper;
import clubbook.backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.NEW_EVENET_REGISTERED_CORRECT, this.eventService.save(newEventDto)));
    }
}
