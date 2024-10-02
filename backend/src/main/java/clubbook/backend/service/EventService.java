package clubbook.backend.service;

import clubbook.backend.dtos.NewEventDto;
import clubbook.backend.model.Event;
import clubbook.backend.model.EventType;
import clubbook.backend.model.EventTypeEnum;
import clubbook.backend.repository.EventRepository;
import clubbook.backend.repository.EventTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventTypeRepository eventTypeRepository;

    @Autowired
    public EventService(EventRepository eventRepository, EventTypeRepository eventTypeRepository) {
        this.eventRepository = eventRepository;
        this.eventTypeRepository = eventTypeRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<EventType> getEventTypes() {
        return eventTypeRepository.findAll();
    }

    public void saveEventType(EventType eventType) {
        this.eventTypeRepository.save(eventType);
    }

    public Boolean save(NewEventDto newEventDto) {
        Event event = new Event();
        event.setTitle(newEventDto.getTitle());
        event.setAddress(newEventDto.getAddress());
        event.setDate(newEventDto.getDate());
        event.setAdditionalInfo(newEventDto.getAdditionalInfo());
        event.setType(this.eventTypeRepository.findById(newEventDto.getType()).orElseThrow());
        event.setBirthYearStart(newEventDto.getBirthYearStart());
        event.setBirthYearEnd(newEventDto.getBirthYearEnd());
        this.eventRepository.save(event);
        return true;
    }
}
