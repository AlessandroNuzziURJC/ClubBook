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

@Validated
@RequestMapping("/event_attendance")
@RestController
public class EventAttendanceController {

    private final EventAttendanceService eventAttendanceService;

    @Autowired
    public EventAttendanceController(EventAttendanceService eventAttendanceService) {
        this.eventAttendanceService = eventAttendanceService;
    }

    @GetMapping("/{eventId}/students")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'TEACHER')")
    public ResponseEntity<ResponseWrapper<List<EventAttendance>>> getStudentAttendance(@PathVariable int eventId) {
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, this.eventAttendanceService.getEventAttendanceStudents(eventId)));
    }

    @GetMapping("/{eventId}/teachers")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'TEACHER')")
    public ResponseEntity<ResponseWrapper<List<EventAttendance>>> getTeacherAttendance(@PathVariable int eventId) {
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, this.eventAttendanceService.getEventAttendanceTeachers(eventId)));
    }

    @GetMapping("/{eventId}/{userId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<ResponseWrapper<EventAttendance>> getUserAttendance(@PathVariable int eventId, @PathVariable int userId) {
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, this.eventAttendanceService.getEventAttendanceByUser(eventId, userId)));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<ResponseWrapper<Boolean>> updateAttendance(@RequestBody UpdateEventAttendanceDto attendance) {
        this.eventAttendanceService.saveEventAttendance(attendance);
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.ATTENDANCE_UPDATED, true));
    }
}
