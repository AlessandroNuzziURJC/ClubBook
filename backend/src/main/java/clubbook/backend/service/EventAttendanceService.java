package clubbook.backend.service;

import clubbook.backend.dtos.EventDto;
import clubbook.backend.dtos.UpdateEventAttendanceDto;
import clubbook.backend.model.*;
import clubbook.backend.model.enumClasses.RoleEnum;
import clubbook.backend.model.notification.DeleteEventNotificationFactory;
import clubbook.backend.model.notification.ModifyEventNotificationFactory;
import clubbook.backend.model.notification.NewEventNotificationFactory;
import clubbook.backend.model.notification.NotificationFactory;
import clubbook.backend.repository.EventAttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class for managing event attendance functionality.
 */
@Service
public class EventAttendanceService {

    private final EventAttendanceRepository eventAttendanceRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final NotificationService notificationService;

    /**
     * Constructs an instance of {@link EventAttendanceService}.
     *
     * @param eventAttendanceRepository the repository for event attendance records
     * @param userService the service for user-related operations
     * @param roleService the service for role-related operations
     * @param notificationService the service for managing notifications
     */
    @Autowired
    public EventAttendanceService(EventAttendanceRepository eventAttendanceRepository, UserService userService, RoleService roleService, NotificationService notificationService){
        this.eventAttendanceRepository = eventAttendanceRepository;
        this.userService = userService;
        this.roleService = roleService;
        this.notificationService = notificationService;
    }

    /**
     * Initializes attendance for the specified event by saving event attendance records
     * for all students and teachers associated with the event.
     *
     * @param event the event for which attendance is to be initialized
     */
    public void initializeAttendance(Event event) {
        Role role = roleService.findByName(RoleEnum.STUDENT);

        List<User> students = this.userService.findUserBornBetween(event.getBirthYearStart(), event.getBirthYearEnd(), role);
        EventAttendance eventAttendance;
        for (User user : students) {
            eventAttendance = new EventAttendance(event, user, null);
            this.eventAttendanceRepository.save(eventAttendance);

            NotificationFactory notificationFactory = new NewEventNotificationFactory(event.getDate(), user);
            notificationFactory.createNotification();
            this.notificationService.save(notificationFactory.getNotification());
        }

        List<User> teachers = this.userService.getAllTeachers();
        for (User user : teachers) {
            eventAttendance = new EventAttendance(event, user, null);
            this.eventAttendanceRepository.save(eventAttendance);

            NotificationFactory notificationFactory = new NewEventNotificationFactory(event.getDate(), user);
            notificationFactory.createNotification();
            this.notificationService.save(notificationFactory.getNotification());
        }

    }

    /**
     * Updates the attendance status for a given event attendance record.
     *
     * @param updateEventAttendanceDto DTO containing the ID and new status for the event attendance
     */
    public void saveEventAttendance(UpdateEventAttendanceDto updateEventAttendanceDto) {
        EventAttendance eventAttendance = this.eventAttendanceRepository.findById(updateEventAttendanceDto.getEventAttendanceId()).orElseThrow();
        eventAttendance.setStatus(updateEventAttendanceDto.getStatus());
        this.eventAttendanceRepository.save(eventAttendance);
    }

    /**
     * Retrieves the event attendance record for a specific user in a given event.
     *
     * @param event the event ID
     * @param user the user ID
     * @return the event attendance record
     */
    public EventAttendance getEventAttendanceByUser(int event, int user) {
        return this.eventAttendanceRepository.findByEventAndUser(event, user).orElseThrow();
    }

    /**
     * Retrieves a list of event attendance records for students associated with a specific event.
     *
     * @param eventId the event ID
     * @return a list of event attendance records for students
     */
    public List<EventAttendance> getEventAttendanceStudents(int eventId) {
        Role role = roleService.findByName(RoleEnum.STUDENT);
        List<EventAttendance> eventAttendances = this.eventAttendanceRepository.findByEventIdAndUserRole(eventId, role);

        return eventAttendances.stream().filter(item -> item.getUser().isAllowedAccess()).toList();
    }

    /**
     * Retrieves a list of event attendance records for teachers associated with a specific event.
     *
     * @param eventId the event ID
     * @return a list of event attendance records for teachers
     */
    public List<EventAttendance> getEventAttendanceTeachers(int eventId) {
        Role role = roleService.findByName(RoleEnum.TEACHER);
        List<EventAttendance> eventAttendances = this.eventAttendanceRepository.findByEventIdAndUserRole(eventId, role);

        return eventAttendances.stream().filter(item -> item.getUser().isAllowedAccess()).toList();
    }

    /**
     * Updates attendance for a specified event based on the provided event data,
     * notifying users of any changes to their attendance status.
     *
     * @param event the event to update attendance for
     * @param eventDto the DTO containing updated event information
     */
    public void updateAttendance(Event event, EventDto eventDto) {
        Role role = roleService.findByName(RoleEnum.STUDENT);

        Set<EventAttendance> setPrevEventAttendance = this.eventAttendanceRepository.findByEventId(event.getId(), role);
        Map<User, EventAttendance> usersPrevEvent = new HashMap<>(setPrevEventAttendance.size());

        for (EventAttendance eventAttendance : setPrevEventAttendance) {
            usersPrevEvent.put(eventAttendance.getUser(), eventAttendance);
        }

        List<User> students = this.userService.findUserBornBetween(eventDto.getBirthYearStart(), eventDto.getBirthYearEnd(), role);

        for (User user : students) {
            if (!usersPrevEvent.containsKey(user)) {
                this.eventAttendanceRepository.save(new EventAttendance(event, user, null));

                NotificationFactory notificationFactory = new NewEventNotificationFactory(event.getDate(), user);
                notificationFactory.createNotification();
                this.notificationService.save(notificationFactory.getNotification());
            } else {
                usersPrevEvent.remove(user);

                NotificationFactory notificationFactory = new ModifyEventNotificationFactory(event.getDate(), user);
                notificationFactory.createNotification();
                this.notificationService.save(notificationFactory.getNotification());
            }
        }

        for (User user : usersPrevEvent.keySet()) {
            this.eventAttendanceRepository.delete(usersPrevEvent.get(user));

            NotificationFactory notificationFactory = new DeleteEventNotificationFactory(event.getDate(), user);
            notificationFactory.createNotification();
            this.notificationService.save(notificationFactory.getNotification());
        }
    }
}
