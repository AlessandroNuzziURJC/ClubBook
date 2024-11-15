package clubbook.backend.controller;

import clubbook.backend.dtos.UpdateEventAttendanceDto;
import clubbook.backend.model.EventAttendance;
import clubbook.backend.responses.ResponseMessages;
import clubbook.backend.responses.ResponseWrapper;
import clubbook.backend.service.EventAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller to manage event attendance.
 * Provides endpoints for querying and updating the attendance of students and teachers for specific events.
 */
@Validated
@RequestMapping("/event_attendance")
@RestController
public class EventAttendanceController {

    private final EventAttendanceService eventAttendanceService;

    @Autowired
    public EventAttendanceController(EventAttendanceService eventAttendanceService) {
        this.eventAttendanceService = eventAttendanceService;
    }

    /**
     * Retrieves the list of student attendance for a specific event.
     *
     * @param eventId The ID of the event for which to retrieve student attendance.
     * @return A response containing the list of student attendance for the event.
     */
    @GetMapping("/{eventId}/students")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'TEACHER')")
    public ResponseEntity<ResponseWrapper<List<EventAttendance>>> getStudentAttendance(@PathVariable int eventId) {
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, this.eventAttendanceService.getEventAttendanceStudents(eventId)));
    }

    /**
     * Retrieves the list of teacher attendance for a specific event.
     *
     * @param eventId The ID of the event for which to retrieve teacher attendance.
     * @return A response containing the list of teacher attendance for the event.
     */
    @GetMapping("/{eventId}/teachers")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'TEACHER')")
    public ResponseEntity<ResponseWrapper<List<EventAttendance>>> getTeacherAttendance(@PathVariable int eventId) {
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, this.eventAttendanceService.getEventAttendanceTeachers(eventId)));
    }

    /**
     * Retrieves the attendance of a specific user for a given event.
     *
     * @param eventId The ID of the event for which to retrieve user attendance.
     * @param userId  The ID of the user whose attendance is to be queried.
     * @return A response containing the user's attendance for the event.
     */
    @GetMapping("/{eventId}/{userId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<ResponseWrapper<EventAttendance>> getUserAttendance(@PathVariable int eventId, @PathVariable int userId) {
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, this.eventAttendanceService.getEventAttendanceByUser(eventId, userId)));
    }

    /**
     * Updates the attendance for a specific event.
     *
     * @param attendance Object containing the attendance information to be updated.
     * @return A response indicating whether the attendance update was successful.
     */
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<ResponseWrapper<Boolean>> updateAttendance(@RequestBody UpdateEventAttendanceDto attendance) {
        this.eventAttendanceService.saveEventAttendance(attendance);
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.ATTENDANCE_UPDATED, true));
    }
}
