package clubbook.backend.service;

import clubbook.backend.dtos.EventDto;
import clubbook.backend.dtos.NewEventDto;
import clubbook.backend.model.Event;
import clubbook.backend.model.EventAttendance;
import clubbook.backend.model.EventType;
import clubbook.backend.model.User;
import clubbook.backend.model.notification.*;
import clubbook.backend.repository.EventRepository;
import clubbook.backend.repository.EventTypeRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for managing events.
 */
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventTypeRepository eventTypeRepository;
    private final EventAttendanceService eventAttendanceService;
    private final UserService userService;
    private final NotificationService notificationService;

    /**
     * Constructor of EventService.
     * @param eventRepository
     * @param eventTypeRepository
     * @param eventAttendanceService
     * @param userService
     * @param notificationService
     */
    @Autowired
    public EventService(EventRepository eventRepository, EventTypeRepository eventTypeRepository, EventAttendanceService eventAttendanceService, UserService userService, NotificationService notificationService) {
        this.eventRepository = eventRepository;
        this.eventTypeRepository = eventTypeRepository;
        this.eventAttendanceService = eventAttendanceService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    /**
     * Retrieves all available event types.
     *
     * @return a list of event types.
     */
    public List<EventType> getEventTypes() {
        return eventTypeRepository.findAll();
    }

    /**
     * Saves a new event type.
     *
     * @param eventType the event type to save.
     */
    public void saveEventType(EventType eventType) {
        this.eventTypeRepository.save(eventType);
    }

    /**
     * Saves a new event based on the provided data transfer object.
     *
     * @param newEventDto the data transfer object containing event details.
     * @return true if the event was saved successfully, false otherwise.
     */
    public Boolean save(NewEventDto newEventDto) {
        if (newEventDto.getDate().isBefore(LocalDate.now())) {
            return false;
        }
        if (newEventDto.getDeadline().isBefore(LocalDate.now()) || newEventDto.getDeadline().isAfter(newEventDto.getDate())) {
            return false;
        }

        Event event = new Event();
        event.setTitle(newEventDto.getTitle());
        event.setAddress(newEventDto.getAddress());
        event.setDate(newEventDto.getDate());
        event.setAdditionalInfo(newEventDto.getAdditionalInfo());
        event.setType(this.eventTypeRepository.findById(newEventDto.getType()).orElseThrow());
        event.setBirthYearStart(newEventDto.getBirthYearStart());
        event.setBirthYearEnd(newEventDto.getBirthYearEnd());
        event.setDeadline(newEventDto.getDeadline());
        this.eventRepository.save(event);
        this.eventAttendanceService.initializeAttendance(event);

        return true;
    }

    /**
     * Updates an existing event based on the provided data transfer object.
     *
     * @param editEventDto the data transfer object containing updated event details.
     * @return true if the event was updated successfully, false otherwise.
     */
    public Boolean saveEdited(EventDto editEventDto) {
        if (editEventDto.getDate().isBefore(LocalDate.now())) {
            return false;
        }
        if (editEventDto.getDeadline().isBefore(LocalDate.now()) || editEventDto.getDeadline().isAfter(editEventDto.getDate())) {
            return false;
        }

        Event event = this.eventRepository.findById(editEventDto.getId()).orElseThrow();
        event.setTitle(editEventDto.getTitle());
        event.setAddress(editEventDto.getAddress());
        event.setDate(editEventDto.getDate());
        event.setAdditionalInfo(editEventDto.getAdditionalInfo());
        event.setType(this.eventTypeRepository.findById(editEventDto.getType().getEventTypeId()).orElseThrow());
        event.setDeadline(editEventDto.getDeadline());
        if (!event.getBirthYearStart().isEqual(editEventDto.getBirthYearStart()) || !event.getBirthYearEnd().isEqual(editEventDto.getBirthYearEnd())) {
            this.eventAttendanceService.updateAttendance(event, editEventDto);
        }
        event.setBirthYearStart(editEventDto.getBirthYearStart());
        event.setBirthYearEnd(editEventDto.getBirthYearEnd());
        this.eventRepository.save(event);

        return true;
    }

    /**
     * Finds all future events.
     *
     * @return a list of data transfer objects for future events.
     */
    public List<EventDto> findAllFutureEvents() {
        List<Event> futureEvents = this.eventRepository.findAllByDateGreaterThanEqualOrderByDate(LocalDate.now());
        return generateListEventDtos(futureEvents);
    }

    /**
     * Finds the next upcoming event.
     *
     * @return a data transfer object for the next event, or null if no upcoming events exist.
     */
    public EventDto findNextEvent() {
        Event event = this.eventRepository.findFirstByDateGreaterThanEqualOrderByDateAsc(LocalDate.now());
        if (event != null)
            return new EventDto(event);
        return null;
    }

    /**
     * Deletes an event and sends notifications to attendees.
     *
     * @param eventId the ID of the event to delete.
     */
    public void deleteEvent(Integer eventId) {
        Event event = this.eventRepository.findById(eventId).orElseThrow();
        for(EventAttendance eventAttendance : event.getAttendances()) {
            NotificationFactory notificationFactory = new DeleteEventNotificationFactory(event.getDate(), eventAttendance.getUser());
            notificationFactory.createNotification();
            this.notificationService.save(notificationFactory.getNotification());
        }
        this.eventRepository.deleteById(eventId);
    }

    /**
     * Finds all past events.
     *
     * @return a list of data transfer objects for past events.
     */
    public List<EventDto> findAllPastEvents() {
        List<Event> futureEvents = this.eventRepository.findAllByDateBeforeOrderByDateAsc(LocalDate.now());
        return generateListEventDtos(futureEvents);
    }

    /**
     * Converts Event list to EventDto list.
     * @param futureEvents
     * @return
     */
    private List<EventDto> generateListEventDtos(List<Event> futureEvents) {
        List<EventDto> eventDtos = new ArrayList<>(futureEvents.size());
        EventDto eventDto;
        for (Event e: futureEvents) {
            eventDto = new EventDto(e);
            eventDtos.add(eventDto);
        }
        return eventDtos;
    }

    /**
     * Deletes all events.
     */
    public void deleteAll() {
        this.eventRepository.deleteAll();
    }

    /**
     * Finds events occurring in the current month.
     *
     * @param monthValue the month value (1-12).
     * @param year the year.
     * @return a map of day of the month to a list of event data transfer objects.
     */
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

    /**
     * Finds a specific event by its ID.
     *
     * @param eventId the ID of the event to find.
     * @return the found event.
     */
    public Event findEvent(int eventId) {
        return this.eventRepository.findById(eventId).orElseThrow();
    }

    /**
     * Finds future events that a specific student can attend.
     *
     * @param userId the ID of the student.
     * @return a list of data transfer objects for future events that the student can attend.
     */
    public List<EventDto> findStudentFutureEvents(int userId) {
        User user = this.userService.findById(userId);
        List<Event> allEventsThatAdmit = this.eventRepository.findAllEventsThatAdmit(user.getBirthday());
        List<EventDto> output = new ArrayList<>(allEventsThatAdmit.size());

        for (Event e: allEventsThatAdmit) {
            output.add(new EventDto(e));
        }
        return output;
    }

    /**
     * Finds the next event that a specific student can attend.
     *
     * @param userId the ID of the student.
     * @return a data transfer object for the next event that the student can attend.
     */
    public EventDto findNextEventStudent(int userId) {
        User user = this.userService.findById(userId);
        Event nextEvent = this.eventRepository.findNextEventThatAdmit(user.getBirthday());
        return new EventDto(nextEvent);
    }

    /**
     * Finds events for a specific student in the current month.
     *
     * @param monthValue the month value (1-12).
     * @param year the year.
     * @param userId the ID of the student.
     * @return a map of day of the month to a list of event data transfer objects.
     */
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

    /**
     * Sends notification Event Reminder.
     */
    @Transactional
    @Scheduled(cron = "0 0 12 * * ?")
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

    /**
     * Sends notification Event Reminder Attendance.
     */
    @Transactional
    @Scheduled(cron = "0 0 11 * * ?")
    public void scheduleEventReminderAttendanceNotifications() {
        List<Event> events = this.eventRepository.findAllEventsDeadlineToday(LocalDate.now());
        for (Event e: events) {
            for (EventAttendance eventAttendance: e.getAttendances()) {
                if (eventAttendance.getStatus()) {
                NotificationFactory notificationFactory = new EventInscriptionLimitNotificationFactory(e.getDate(), eventAttendance.getUser());
                notificationFactory.createNotification();
                this.notificationService.save(notificationFactory.getNotification());
                }
            }
        }
    }

    /**
     * Sends notification Finish Inscription.
     */
    @Transactional
    @Scheduled(cron = "0 1 0 * * ?")
    public void scheduleEventFinishInscriptionNotifications() {
        List<Event> events = this.eventRepository.findAllEventsInscriptionFinished(LocalDate.now().minusDays(1));
        List<User> administrators = this.userService.findAllAdministrators();
        List<User> teachers = this.userService.getAllTeachers();

        for (Event e: events) {
            for (EventAttendance eventAttendance: e.getAttendances()) {
                if (eventAttendance.getStatus() == null)
                    eventAttendance.setStatus(false);
            }
            eventRepository.save(e);

            for (User u: administrators) {
                NotificationFactory notificationFactory = new EventInscriptionFinishedNotificationFactory(e.getDate(), u);
                notificationFactory.createNotification();
                this.notificationService.save(notificationFactory.getNotification());
            }

            for (User u: teachers) {
                NotificationFactory notificationFactory = new EventInscriptionFinishedNotificationFactory(e.getDate(), u);
                notificationFactory.createNotification();
                this.notificationService.save(notificationFactory.getNotification());
            }
        }



    }

    /**
     * Generates a PDF containing the details of events.
     *
     * @return a byte array representing the PDF document.
     * @throws DocumentException if an error occurs while creating the PDF document.
     */
    public byte[] generatePdf(int eventId) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);

        try {
            Event event = this.eventRepository.findById(eventId).orElseThrow();

            PdfWriter.getInstance(document, out);
            document.open();

            List<EventAttendance> eventAttendances = event.getAttendances();

            List<EventAttendance> attendances = eventAttendances.stream()
                    .filter(att -> att.getStatus() != null && att.getStatus())
                    .filter(item -> item.getUser().isAllowedAccess())
                    .toList();

            List<EventAttendance> pendingAttendances = eventAttendances.stream()
                    .filter(att -> att.getStatus() == null)
                    .filter(item -> item.getUser().isAllowedAccess())
                    .toList();

            List<EventAttendance> notAttendances = eventAttendances.stream()
                    .filter(att -> att.getStatus() != null && !att.getStatus())
                    .filter(item -> item.getUser().isAllowedAccess())
                    .toList();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
            Font indentFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.DARK_GRAY);

            Paragraph eventTitle = new Paragraph("Evento: " + event.getTitle(), titleFont);
            document.add(eventTitle);

            Paragraph eventDate = new Paragraph("Fecha: " + event.getDate(), normalFont);
            document.add(eventDate);

            document.add(new Paragraph(" "));

            Paragraph attendeesTitle = new Paragraph("Asistentes:", subtitleFont);
            document.add(attendeesTitle);
            for (EventAttendance att : attendances) {
                Paragraph attendeeName = new Paragraph("-" + att.getUser().getRole().getName().name() + ": " + att.getUser().getFirstName() + " " + att.getUser().getLastName(), indentFont);
                attendeeName.setIndentationLeft(20);
                document.add(attendeeName);
            }

            document.newPage();

            Paragraph pendingAttendeesTitle = new Paragraph("Sin confirmar:", subtitleFont);
            document.add(pendingAttendeesTitle);
            for (EventAttendance att : pendingAttendances) {
                Paragraph attendeeName = new Paragraph("-" + att.getUser().getRole().getName().name() + ": " + att.getUser().getFirstName() + " " + att.getUser().getLastName(), indentFont);
                attendeeName.setIndentationLeft(20);
                document.add(attendeeName);
            }

            document.newPage();

            Paragraph nonAttendeesTitle = new Paragraph("No Asistentes:", subtitleFont);
            document.add(nonAttendeesTitle);
            for (EventAttendance att : notAttendances) {
                Paragraph nonAttendeeName = new Paragraph("-" + att.getUser().getRole().getName().name() + ": " + att.getUser().getFirstName() + " " + att.getUser().getLastName(), indentFont);
                nonAttendeeName.setIndentationLeft(20);
                document.add(nonAttendeeName);
            }

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

        return out.toByteArray();
    }
}
