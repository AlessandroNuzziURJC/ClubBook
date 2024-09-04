package clubbook.backend.controller;

import clubbook.backend.dtos.AttendanceDto;
import clubbook.backend.dtos.ClassGroupAttendanceDto;
import clubbook.backend.dtos.YearsDto;
import clubbook.backend.model.Attendance;
import clubbook.backend.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/attendance")
@RestController
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<AttendanceDto> saveAttendances(@RequestBody AttendanceDto attendanceDto) {
        return ResponseEntity.ok(this.attendanceService.saveAll(attendanceDto));
    }

    @GetMapping("/{year}/{month}/{classGroupId}")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ClassGroupAttendanceDto> getAttendances(@PathVariable String year, @PathVariable String month, @PathVariable String classGroupId) {
        return ResponseEntity.ok(this.attendanceService.getClassGroupAttendanceWithYearAndMonth(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(classGroupId)));
    }

    @GetMapping("/dates/{classGroupId}")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<List<YearsDto>> getYears(@PathVariable String classGroupId) {
        return ResponseEntity.ok(this.attendanceService.getYears(Integer.parseInt(classGroupId)));
    }
}
