package clubbook.backend.controller;

import clubbook.backend.dtos.RegisterClassGroupDto;
import clubbook.backend.model.ClassGroup;
import clubbook.backend.service.ClassGroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController()
public class ClassGroupController {

    private final ClassGroupService classGroupService;


    @Autowired
    public ClassGroupController(ClassGroupService classGroupService) {
        this.classGroupService = classGroupService;
    }

    @GetMapping("/classGroup")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<List<ClassGroup>> getAllClassGroups() {
        List<ClassGroup> classGroupList = this.classGroupService.getAllClassGroups();
        if (classGroupList == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(classGroupList);
    }

    @GetMapping("/{id}/classGroup")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<ClassGroup> getClassGroup(@PathVariable int id) {
        ClassGroup classGroupList = this.classGroupService.getClassGroup(id);
        if (classGroupList == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(classGroupList);
    }

    @PostMapping("/classGroup")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<ClassGroup> createClassGroup(@Valid @RequestBody RegisterClassGroupDto registerClassGroupDto) {
        ClassGroup classGroup = classGroupService.create(registerClassGroupDto);
        return ResponseEntity.ok(classGroup);
    }

    @DeleteMapping("/{id}/classGroup")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity deleteClassGroup(@PathVariable int id) {
        classGroupService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/classGroup")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<ClassGroup> modifyClassGroup(@Valid @RequestBody RegisterClassGroupDto classGroupDto, @PathVariable int id) {
        ClassGroup classGroup = classGroupService.findById(id);
        ClassGroup newClassGroup = classGroupService.update(classGroup, classGroupDto);
        return ResponseEntity.ok(newClassGroup);
    }

}
