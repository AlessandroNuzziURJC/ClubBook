package clubbook.backend.controller;

import clubbook.backend.dtos.RegisterClassGroupDto;
import clubbook.backend.model.ClassGroup;
import clubbook.backend.model.User;
import clubbook.backend.responses.ResponseMessages;
import clubbook.backend.responses.ResponseWrapper;
import clubbook.backend.service.ClassGroupService;
import clubbook.backend.service.SeasonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Validated
@RestController()
public class ClassGroupController {

    private final ClassGroupService classGroupService;
    private final SeasonService seasonService;


    @Autowired
    public ClassGroupController(ClassGroupService classGroupService, SeasonService seasonService) {
        this.classGroupService = classGroupService;
        this.seasonService = seasonService;
    }

    @GetMapping("/classGroup")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'TEACHER')")
    public ResponseEntity<ResponseWrapper<List<ClassGroup>>> getAllClassGroups() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!this.seasonService.seasonStarted() && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_TEACHER"))){
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
        }

        List<ClassGroup> classGroupList = this.classGroupService.getAllClassGroups();
        if (classGroupList == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, classGroupList));
    }

    @GetMapping("/{id}/classGroup")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'TEACHER')")
    public ResponseEntity<ResponseWrapper<ClassGroup>> getClassGroup(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!this.seasonService.seasonStarted() && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_TEACHER"))){
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
        }

        ClassGroup classGroup = this.classGroupService.getClassGroup(id);
        if (classGroup == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, classGroup));
    }

    @PostMapping("/classGroup")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<ResponseWrapper<ClassGroup>> createClassGroup(@Valid @RequestBody RegisterClassGroupDto registerClassGroupDto) {
        if (!this.seasonService.seasonStarted()){
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
        }

        ClassGroup classGroup = classGroupService.create(registerClassGroupDto);
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.CORRECT_REGISTER, classGroup));
    }

    @DeleteMapping("/{id}/classGroup")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity deleteClassGroup(@PathVariable int id) {
        if (!this.seasonService.seasonStarted()){
            return ResponseEntity.badRequest().build();
        }

        classGroupService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/classGroup")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<ClassGroup> modifyClassGroup(@Valid @RequestBody RegisterClassGroupDto classGroupDto, @PathVariable int id) {
        if (!this.seasonService.seasonStarted()){
            return ResponseEntity.badRequest().build();
        }

        ClassGroup classGroup = classGroupService.findById(id);
        ClassGroup newClassGroup = classGroupService.update(classGroup, classGroupDto);
        return ResponseEntity.ok(newClassGroup);
    }

    @PostMapping("/{id}/addStudents")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<List<User>> addNewStudentsClassGroup(@PathVariable int id, @RequestBody List<Integer> studentsIds) {
        if (!this.seasonService.seasonStarted()){
            return ResponseEntity.badRequest().build();
        }

        List<User> users = classGroupService.addNewStudentsClassGroup(id, studentsIds);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}/removeStudents")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<List<User>> removeStudentsClassGroup(@PathVariable int id, @RequestBody List<Integer> studentsIds) {
        if (!this.seasonService.seasonStarted()){
            return ResponseEntity.badRequest().build();
        }

        List<User> users = classGroupService.removeStudentsClassGroup(id, studentsIds);
        return ResponseEntity.ok(users);
    }
}
