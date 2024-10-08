package clubbook.backend.service;

import clubbook.backend.dtos.EventDto;
import clubbook.backend.dtos.NewEventDto;
import clubbook.backend.model.Event;
import clubbook.backend.model.EventType;
import clubbook.backend.repository.EventRepository;
import clubbook.backend.repository.EventTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventTypeRepository eventTypeRepository;
    private final EventAttendanceService eventAttendanceService;

    @Autowired
    public EventService(EventRepository eventRepository, EventTypeRepository eventTypeRepository, EventAttendanceService eventAttendanceService) {
        this.eventRepository = eventRepository;
        this.eventTypeRepository = eventTypeRepository;
        this.eventAttendanceService = eventAttendanceService;
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
        if (newEventDto.getDate().isBefore(LocalDate.now())) {
            return false;
        }
        event.setDate(newEventDto.getDate());
        event.setAdditionalInfo(newEventDto.getAdditionalInfo());
        event.setType(this.eventTypeRepository.findById(newEventDto.getType()).orElseThrow());
        event.setBirthYearStart(newEventDto.getBirthYearStart());
        event.setBirthYearEnd(newEventDto.getBirthYearEnd());
        this.eventRepository.save(event);
        this.eventAttendanceService.initializeAttendance(event);

        return true;
    }

    public Boolean saveEdited(EventDto editEventDto) {
        Event prevEvent = this.eventRepository.findById(editEventDto.getId()).orElseThrow();

        Event event = this.eventRepository.findById(editEventDto.getId()).orElseThrow();
        event.setTitle(editEventDto.getTitle());
        event.setAddress(editEventDto.getAddress());
        if (editEventDto.getDate().isBefore(LocalDate.now())) {
            return false;
        }
        event.setDate(editEventDto.getDate());
        event.setAdditionalInfo(editEventDto.getAdditionalInfo());
        event.setType(this.eventTypeRepository.findById(editEventDto.getType().getEventTypeId()).orElseThrow());
        event.setBirthYearStart(editEventDto.getBirthYearStart());
        event.setBirthYearEnd(editEventDto.getBirthYearEnd());
        this.eventRepository.save(event);

        if (!prevEvent.getBirthYearStart().isEqual(editEventDto.getBirthYearStart()) || !prevEvent.getBirthYearEnd().isEqual(editEventDto.getBirthYearEnd())) {
            this.eventAttendanceService.updateAttendance(prevEvent, event);
        }
        return true;
    }

    public List<EventDto> findAllFutureEvents() {
        List<Event> futureEvents = this.eventRepository.findAllByDateGreaterThanEqualOrderByDate(LocalDate.now());
        return generateListEventDtos(futureEvents);
    }

    public EventDto findNextEvent() {
        Event event = this.eventRepository.findFirstByDateGreaterThanEqualOrderByDateAsc(LocalDate.now());
        if (event != null)
            return new EventDto(event);
        return null;
    }

    public boolean deleteEvent(Integer eventId) {
        this.eventRepository.deleteById(eventId);
        return true;
    }

    public List<EventDto> findAllPastEvents() {
        List<Event> futureEvents = this.eventRepository.findAllByDateBeforeOrderByDateAsc(LocalDate.now());
        return generateListEventDtos(futureEvents);
    }

    private List<EventDto> generateListEventDtos(List<Event> futureEvents) {
        List<EventDto> eventDtos = new ArrayList<>(futureEvents.size());
        EventDto eventDto;
        for (Event e: futureEvents) {
            eventDto = new EventDto(e);
            eventDtos.add(eventDto);
        }
        return eventDtos;
    }

    public void deleteAll() {
        this.eventRepository.deleteAll();
    }

    public Map<Integer, List<EventDto>> findActualMonthEvents(int monthValue, int year) {
        List<Event> futureEvents = this.eventRepository.findAllByCurrentMonth(monthValue, year);
        Map<Integer, List<EventDto>> map = new HashMap<>(futureEvents.size());
        for (Event e: futureEvents) {
            if (!map.containsKey(e.getDate().getDayOfMonth()))
                map.put(e.getDate().getDayOfMonth(), new ArrayList<>());
            map.get(e.getDate().getDayOfMonth()).add(new EventDto(e));
        }
        return map;
    }

    public Event findEvent(int eventId) {
        return this.eventRepository.findById(eventId).orElseThrow();
    }
}
