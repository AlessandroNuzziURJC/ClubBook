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


/**
 * Controller for managing attendance-related operations.
 * Provides endpoints for saving attendance records, retrieving attendance data,
 * and generating PDF reports for class group attendance.
 */
@RequestMapping("/attendance")
@RestController
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final ClassGroupService classGroupService;
    private final SeasonService seasonService;

    /**
     * Constructs an AttendanceController with the specified services.
     *
     * @param attendanceService the service for managing attendance data.
     * @param classGroupService  the service for managing class group data.
     * @param seasonService      the service for managing seasons.
     */
    @Autowired
    public AttendanceController(AttendanceService attendanceService, ClassGroupService classGroupService, SeasonService seasonService) {
        this.attendanceService = attendanceService;
        this.classGroupService = classGroupService;
        this.seasonService = seasonService;
    }

    /**
     * Saves attendance records for a specific attendance DTO.
     * Requires the user to have the role of 'TEACHER'.
     *
     * @param attendanceDto the DTO containing attendance data to be saved.
     * @return a ResponseEntity containing a response wrapper with the result of the operation.
     */
    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseEntity<ResponseWrapper<AttendanceDto>> saveAttendances(@RequestBody AttendanceDto attendanceDto) {
        if (!this.seasonService.seasonStarted()){
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
        }
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.ASSISTANCE_REGISTERED_CORRECT, this.attendanceService.saveAll(attendanceDto)));
    }

    /**
     * Retrieves attendance records for a specific month and class group ID.
     * Requires the user to have the role of 'TEACHER' or 'ADMINISTRATOR'.
     *
     * @param month        the month for which attendance records are requested.
     * @param classGroupId the ID of the class group for which attendance records are requested.
     * @return a ResponseEntity containing a response wrapper with the attendance data for the specified class group and month.
     */
    @GetMapping("/{month}/{classGroupId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMINISTRATOR')")
    public ResponseEntity<ResponseWrapper<ClassGroupAttendanceDto>> getAttendances(@PathVariable String month, @PathVariable String classGroupId) {
        if (!this.seasonService.seasonStarted()){
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
        }
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, this.attendanceService.getClassGroupAttendanceWithYearAndMonth(Integer.parseInt(month), Integer.parseInt(classGroupId))));
    }

    /**
     * Generates a PDF report of attendance for a specific class group.
     * Requires the user to have the role of 'TEACHER' or 'ADMINISTRATOR'.
     *
     * @param classGroupId the ID of the class group for which the PDF report is generated.
     * @return a ResponseEntity containing the PDF report as a byte array.
     */
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
