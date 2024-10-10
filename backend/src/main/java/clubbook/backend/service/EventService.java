package clubbook.backend.service;

import clubbook.backend.dtos.EventDto;
import clubbook.backend.dtos.NewEventDto;
import clubbook.backend.model.Event;
import clubbook.backend.model.EventAttendance;
import clubbook.backend.model.EventType;
import clubbook.backend.model.User;
import clubbook.backend.model.notification.DeleteEventNotificationFactory;
import clubbook.backend.model.notification.EventReminderNotificationFactory;
import clubbook.backend.model.notification.NotificationFactory;
import clubbook.backend.repository.EventRepository;
import clubbook.backend.repository.EventTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public EventService(EventRepository eventRepository, EventTypeRepository eventTypeRepository, EventAttendanceService eventAttendanceService, UserService userService, NotificationService notificationService) {
        this.eventRepository = eventRepository;
        this.eventTypeRepository = eventTypeRepository;
        this.eventAttendanceService = eventAttendanceService;
        this.userService = userService;
        this.notificationService = notificationService;
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
        Event event = this.eventRepository.findById(editEventDto.getId()).orElseThrow();
        event.setTitle(editEventDto.getTitle());
        event.setAddress(editEventDto.getAddress());
        if (editEventDto.getDate().isBefore(LocalDate.now())) {
            return false;
        }
        event.setDate(editEventDto.getDate());
        event.setAdditionalInfo(editEventDto.getAdditionalInfo());
        event.setType(this.eventTypeRepository.findById(editEventDto.getType().getEventTypeId()).orElseThrow());
        if (!event.getBirthYearStart().isEqual(editEventDto.getBirthYearStart()) || !event.getBirthYearEnd().isEqual(editEventDto.getBirthYearEnd())) {
            this.eventAttendanceService.updateAttendance(event, editEventDto);
        }
        event.setBirthYearStart(editEventDto.getBirthYearStart());
        event.setBirthYearEnd(editEventDto.getBirthYearEnd());
        this.eventRepository.save(event);

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
        Event event = this.eventRepository.findById(eventId).orElseThrow();
        for(EventAttendance eventAttendance : event.getAttendances()) {
            NotificationFactory notificationFactory = new DeleteEventNotificationFactory(event.getDate(), eventAttendance.getUser());
            notificationFactory.createNotification();
            this.notificationService.save(notificationFactory.getNotification());
        }
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

    public List<EventDto> findStudentFutureEvents(int userId) {
        User user = this.userService.findById(userId);
        List<Event> allEventsThatAdmit = this.eventRepository.findAllEventsThatAdmit(user.getBirthday());
        List<EventDto> output = new ArrayList<>(allEventsThatAdmit.size());

        for (Event e: allEventsThatAdmit) {
            output.add(new EventDto(e));
        }
        return output;
    }

    public EventDto findNextEventStudent(int userId) {
        User user = this.userService.findById(userId);
        Event nextEvent = this.eventRepository.findNextEventThatAdmit(user.getBirthday());
        return new EventDto(nextEvent);
    }

    public Map<Integer, List<EventDto>> findActualMonthEventsStudent(int monthValue, int year, int userId) {
        User user = this.userService.findById(userId);
        List<Event> futureEvents = this.eventRepository.findAllByCurrentMonthForStudent(monthValue, year, user.getBirthday());
        Map<Integer, List<EventDto>> map = new HashMap<>(futureEvents.size());
        for (Event e: futureEvents) {
            if (!map.containsKey(e.getDate().getDayOfMonth()))
                map.put(e.getDate().getDayOfMonth(), new ArrayList<>());
            map.get(e.getDate().getDayOfMonth()).add(new EventDto(e));
        }
        return map;
    }

    @Transactional
    @Scheduled(cron = "0 5 12 * * ?")
    public void scheduleEventReminderNotifications() {
        List<Event> events = this.eventRepository.findAllEventsInNextTwoDays(LocalDate.now(), LocalDate.now().plusDays(2));
        for (Event e: events) {
            for (EventAttendance eventAttendance: e.getAttendances()) {
                NotificationFactory notificationFactory = new EventReminderNotificationFactory(e.getDate(), eventAttendance.getUser());
                notificationFactory.createNotification();
                this.notificationService.save(notificationFactory.getNotification());
            }
        }
    }
}
