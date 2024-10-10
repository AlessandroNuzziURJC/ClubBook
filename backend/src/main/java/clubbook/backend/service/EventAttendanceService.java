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

import java.time.LocalDate;
import java.util.*;

@Service
public class EventAttendanceService {

    private final EventAttendanceRepository eventAttendanceRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final NotificationService notificationService;

    @Autowired
    public EventAttendanceService(EventAttendanceRepository eventAttendanceRepository, UserService userService, RoleService roleService, NotificationService notificationService){
        this.eventAttendanceRepository = eventAttendanceRepository;
        this.userService = userService;
        this.roleService = roleService;
        this.notificationService = notificationService;
    }

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

    public void saveEventAttendance(UpdateEventAttendanceDto updateEventAttendanceDto) {
        EventAttendance eventAttendance = this.eventAttendanceRepository.findById(updateEventAttendanceDto.getEventAttendanceId()).orElseThrow();
        eventAttendance.setStatus(updateEventAttendanceDto.getStatus());
        this.eventAttendanceRepository.save(eventAttendance);
    }

    public EventAttendance getEventAttendanceByUser(int event, int user) {
        return this.eventAttendanceRepository.findByEventAndUser(event, user).orElseThrow();
    }

    public List<EventAttendance> getEventAttendanceStudents(int eventId) {
        Role role = roleService.findByName(RoleEnum.STUDENT);
        return this.eventAttendanceRepository.findByEventIdAndUserRole(eventId, role);
    }

    public List<EventAttendance> getEventAttendanceTeachers(int eventId) {
        Role role = roleService.findByName(RoleEnum.TEACHER);
        return this.eventAttendanceRepository.findByEventIdAndUserRole(eventId, role);
    }

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
