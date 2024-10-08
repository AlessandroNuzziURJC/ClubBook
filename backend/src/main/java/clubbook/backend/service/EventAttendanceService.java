package clubbook.backend.service;

import clubbook.backend.dtos.UpdateEventAttendanceDto;
import clubbook.backend.model.*;
import clubbook.backend.repository.EventAttendanceRepository;
import clubbook.backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EventAttendanceService {

    private final EventAttendanceRepository eventAttendanceRepository;
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public EventAttendanceService(EventAttendanceRepository eventAttendanceRepository, UserService userService, RoleService roleService){
        this.eventAttendanceRepository = eventAttendanceRepository;
        this.userService = userService;
        this.roleService = roleService;
    }

    public void initializeAttendance(Event event) {
        Role role = roleService.findByName(RoleEnum.STUDENT);

        List<User> students = this.userService.findUserBornBetween(event.getBirthYearStart(), event.getBirthYearEnd(), role);
        EventAttendance eventAttendance;
        for (User user : students) {
            eventAttendance = new EventAttendance(event, user, null);
            this.eventAttendanceRepository.save(eventAttendance);
            //Notificar nuevo evento
        }

        List<User> teachers = this.userService.getAllTeachers();
        for (User user : teachers) {
            eventAttendance = new EventAttendance(event, user, null);
            this.eventAttendanceRepository.save(eventAttendance);
            //Notificar nuevo evento
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

    public void updateAttendance(Event prevEvent, Event event) {
        Role role = roleService.findByName(RoleEnum.STUDENT);

        Set<EventAttendance> setPrevEventAttendance = this.eventAttendanceRepository.findByEventId(prevEvent.getId(), role);
        Map<User, EventAttendance> usersPrevEvent = new HashMap<>(setPrevEventAttendance.size());

        for (EventAttendance eventAttendance : setPrevEventAttendance) {
            usersPrevEvent.put(eventAttendance.getUser(), eventAttendance);
        }

        List<User> students = this.userService.findUserBornBetween(event.getBirthYearStart(), event.getBirthYearEnd(), role);

        for (User user : students) {
            if (!usersPrevEvent.containsKey(user)) {
                this.eventAttendanceRepository.save(new EventAttendance(event, user, null));
                //Notificar nuevo evento
            } else {
                usersPrevEvent.remove(user);
            }
        }

        for (User user : usersPrevEvent.keySet()) {
            this.eventAttendanceRepository.delete(usersPrevEvent.get(user));
            //Notificar que se ha editado y ya no tiene acceso al evento
        }
    }
}