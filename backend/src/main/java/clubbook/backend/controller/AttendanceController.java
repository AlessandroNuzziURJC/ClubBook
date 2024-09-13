package clubbook.backend.controller;

import clubbook.backend.dtos.AttendanceDto;
import clubbook.backend.dtos.ClassGroupAttendanceDto;
import clubbook.backend.model.ClassGroup;
import clubbook.backend.responses.ResponseMessages;
import clubbook.backend.responses.ResponseWrapper;
import clubbook.backend.service.AttendanceService;
import clubbook.backend.service.ClassGroupService;
import clubbook.backend.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/attendance")
@RestController
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final ClassGroupService classGroupService;
    private final SeasonService seasonService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService, ClassGroupService classGroupService, SeasonService seasonService) {
        this.attendanceService = attendanceService;
        this.classGroupService = classGroupService;
        this.seasonService = seasonService;
    }

    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ResponseWrapper<AttendanceDto>> saveAttendances(@RequestBody AttendanceDto attendanceDto) {
        if (!this.seasonService.seasonStarted()){
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
        }
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.ASSISTANCE_REGISTERED_CORRECT, this.attendanceService.saveAll(attendanceDto)));
    }

    @GetMapping("/{month}/{classGroupId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMINISTRATOR')")
    public ResponseEntity<ResponseWrapper<ClassGroupAttendanceDto>> getAttendances(@PathVariable String month, @PathVariable String classGroupId) {
        if (!this.seasonService.seasonStarted()){
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
        }
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, this.attendanceService.getClassGroupAttendanceWithYearAndMonth(Integer.parseInt(month), Integer.parseInt(classGroupId))));
    }

    @GetMapping("/generatepdf/{classGroupId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMINISTRATOR')")
    public ResponseEntity<byte[]> generatePdf(@PathVariable String classGroupId) {
        if (!this.seasonService.seasonStarted()){
            return ResponseEntity.badRequest().build();
        }

        byte[] output = this.attendanceService.generatePdf(Integer.parseInt(classGroupId));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        ClassGroup classgroup = classGroupService.findById(Integer.parseInt(classGroupId));
        headers.setContentDispositionFormData("filename", "Attendance_ClassGroup_" + classgroup.getName() + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(output);

    }
}
