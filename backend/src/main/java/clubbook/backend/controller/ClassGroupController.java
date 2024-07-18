package clubbook.backend.controller;

import clubbook.backend.dtos.RegisterClassGroupDto;
import clubbook.backend.dtos.ScheduleDto;
import clubbook.backend.model.ClassGroup;
import clubbook.backend.model.Schedule;
import clubbook.backend.model.User;
import clubbook.backend.service.ClassGroupService;
import clubbook.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Validated
@RestController()
public class ClassGroupController {

    private final ClassGroupService classGroupService;


    @Autowired
    public ClassGroupController(ClassGroupService classGroupService) {
        this.classGroupService = classGroupService;
    }

    @GetMapping("/class")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<List<ClassGroup>> getAllClasses() {
        List<ClassGroup> classGroupList = this.classGroupService.getAllClassGroups();
        if (classGroupList == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(classGroupList);
    }

    @PostMapping("/class")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<ClassGroup> createClass(@Valid @RequestBody RegisterClassGroupDto registerClassGroupDto) {
        ClassGroup classGroup = classGroupService.create(registerClassGroupDto);
        return ResponseEntity.ok(classGroup);
    }
}
