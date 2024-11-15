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

import java.util.List;

/**
 * Controller for managing class groups, including retrieving, creating,
 * updating, and deleting class groups and managing students within them.
 */
@Validated
@RestController()
public class ClassGroupController {

    private final ClassGroupService classGroupService;
    private final SeasonService seasonService;

    /**
     * Constructs a ClassGroupController with the specified services.
     *
     * @param classGroupService the service for handling class group operations.
     * @param seasonService     the service for handling season-related operations.
     */
    @Autowired
    public ClassGroupController(ClassGroupService classGroupService, SeasonService seasonService) {
        this.classGroupService = classGroupService;
        this.seasonService = seasonService;
    }

    /**
     * Retrieves all class groups. Accessible to users with roles 'ADMINISTRATOR' or 'TEACHER'.
     *
     * @return a ResponseEntity containing a response wrapper with the list of class groups.
     */
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

    /**
     * Retrieves a specific class group by its ID. Accessible to users with roles 'ADMINISTRATOR' or 'TEACHER'.
     *
     * @param id the ID of the class group to retrieve.
     * @return a ResponseEntity containing a response wrapper with the class group data.
     */
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

    /**
     * Creates a new class group. Accessible only to users with the role 'ADMINISTRATOR'.
     *
     * @param registerClassGroupDto the DTO containing the class group registration data.
     * @return a ResponseEntity containing a response wrapper with the created class group data.
     */
    @PostMapping("/classGroup")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<ResponseWrapper<ClassGroup>> createClassGroup(@Valid @RequestBody RegisterClassGroupDto registerClassGroupDto) {
        ClassGroup classGroup = classGroupService.create(registerClassGroupDto);
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.CORRECT_REGISTER, classGroup));
    }

    /**
     * Deletes a class group by its ID. Accessible only to users with the role 'ADMINISTRATOR'.
     *
     * @param id the ID of the class group to delete.
     * @return a ResponseEntity with no content (HTTP 200).
     */
    @DeleteMapping("/{id}/classGroup")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<Void> deleteClassGroup(@PathVariable int id) {
        classGroupService.delete(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Modifies an existing class group. Accessible only to users with the role 'ADMINISTRATOR'.
     *
     * @param classGroupDto the DTO containing the new class group data.
     * @param id           the ID of the class group to modify.
     * @return a ResponseEntity containing the updated class group data.
     */
    @PutMapping("/{id}/classGroup")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<ClassGroup> modifyClassGroup(@Valid @RequestBody RegisterClassGroupDto classGroupDto, @PathVariable int id) {
        ClassGroup classGroup = classGroupService.findById(id);
        ClassGroup newClassGroup = classGroupService.update(classGroup, classGroupDto);
        return ResponseEntity.ok(newClassGroup);
    }

    /**
     * Adds new students to a class group. Accessible only to users with the role 'ADMINISTRATOR'.
     *
     * @param id          the ID of the class group to which students will be added.
     * @param studentsIds the list of IDs of students to add.
     * @return a ResponseEntity containing the list of added students.
     */
    @PostMapping("/{id}/addStudents")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<List<User>> addNewStudentsClassGroup(@PathVariable int id, @RequestBody List<Integer> studentsIds) {
        List<User> users = classGroupService.addNewStudentsClassGroup(id, studentsIds);
        return ResponseEntity.ok(users);
    }

    /**
     * Removes students from a class group. Accessible only to users with the role 'ADMINISTRATOR'.
     *
     * @param id          the ID of the class group from which students will be removed.
     * @param studentsIds the list of IDs of students to remove.
     * @return a ResponseEntity containing the list of remaining students.
     */
    @PutMapping("/{id}/removeStudents")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<List<User>> removeStudentsClassGroup(@PathVariable int id, @RequestBody List<Integer> studentsIds) {
        List<User> users = classGroupService.removeStudentsClassGroup(id, studentsIds);
        return ResponseEntity.ok(users);
    }
}
