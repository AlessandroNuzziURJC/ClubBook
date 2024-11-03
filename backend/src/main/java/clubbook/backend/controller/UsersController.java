package clubbook.backend.controller;

import clubbook.backend.dtos.UpdateUserDto;
import clubbook.backend.model.User;
import clubbook.backend.responses.ResponseMessages;
import clubbook.backend.responses.ResponseWrapper;
import clubbook.backend.service.SeasonService;
import clubbook.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * REST controller for managing user-related operations, including
 * retrieving, updating, and deleting user information.
 */
@RestController()
public class UsersController {

    private final UserService userService;
    private final SeasonService seasonService;

    /**
     * Constructor to inject dependencies.
     *
     * @param userService   Service to handle user-related operations.
     * @param seasonService Service to handle season-related operations.
     */
    @Autowired
    public UsersController(UserService userService, SeasonService seasonService) {
        this.userService = userService;
        this.seasonService = seasonService;
    }

    /**
     * Retrieves all users.
     *
     * @return HTTP response with a list of all users.
     */
    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<ResponseWrapper<List<User>>> getAllUsers() {
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, userService.getAllUsers()));
    }

    /**
     * Retrieves a paginated list of all students.
     *
     * @param pageNumber Page number to retrieve (default 0).
     * @param pageSize   Number of students per page (default 10).
     * @return HTTP response with a paginated list of students.
     */
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'TEACHER')")
    @GetMapping("/students")
    public ResponseEntity<ResponseWrapper<Page<User>>> getAllStudents(@RequestParam(defaultValue = "0") int pageNumber,
                                     @RequestParam(defaultValue = "10") int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!this.seasonService.seasonStarted() && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_TEACHER"))){
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(ResponseMessages.SEASON_NOT_STARTED, null));
        }

        Page<User> page = userService.getStudentsPage(pageNumber, pageSize);
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, page));
    }

    /**
     * Retrieves all students without a class group.
     *
     * @return HTTP response with a list of students without a class group.
     */
    @GetMapping("/studentsWithoutClassGroup")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<ResponseWrapper<List<User>>> getAllStudentsWithoutClassGroup() {
        List<User> users = userService.getAllStudentsWithoutClassGroup();
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, users));
    }

    /**
     * Searches for students by name.
     *
     * @param search The search term to filter students by name.
     * @return HTTP response with a list of students matching the search criteria.
     */
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'TEACHER')")
    @GetMapping("/studentsSearch")
    public ResponseEntity<ResponseWrapper<List<User>>> getStudentsListFilteredByName(@RequestParam String search) {
        List<User> list = userService.getStudentsListFilteredByName(search);
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, list));
    }

    /**
     * Retrieves a paginated list of all teachers.
     *
     * @param pageNumber Page number to retrieve (default 0).
     * @param pageSize   Number of teachers per page (default 10).
     * @return HTTP response with a paginated list of teachers.
     */
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @GetMapping("/teachers")
    public ResponseEntity<ResponseWrapper<Page<User>>> getAllTeachers(@RequestParam(defaultValue = "0") int pageNumber,
                                                     @RequestParam(defaultValue = "10") int pageSize) {
        Page<User> page = userService.getTeachersPage(pageNumber, pageSize);
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, page));
    }

    /**
     * Retrieves a list of all teachers.
     *
     * @return HTTP response with a list of all teachers.
     */
    @PreAuthorize(("hasAnyRole('ADMINISTRATOR')"))
    @GetMapping("/allTeachers")
    public ResponseEntity<ResponseWrapper<List<User>>> getAllTeachers() {
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, userService.getAllTeachers()));
    }

    /**
     * Searches for teachers by name.
     *
     * @param search The search term to filter teachers by name.
     * @return HTTP response with a list of teachers matching the search criteria.
     */
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @GetMapping("/teachersSearch")
    public ResponseEntity<ResponseWrapper<List<User>>> getTeachersListFilteredByName(@RequestParam String search) {
        List<User> list = userService.getTeachersListFilteredByName(search);
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, list));
    }

    /**
     * Retrieves a list of all administrators except the specified one.
     *
     * @param id The ID of the administrator to exclude.
     * @return HTTP response with a list of all administrators except the specified one.
     */
    @PreAuthorize(("hasAnyRole('ADMINISTRATOR')"))
    @GetMapping("/administrator/all/{id}")
    public ResponseEntity<ResponseWrapper<List<User>>> getAllAdministrators(@PathVariable String id) {
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, this.userService.findAllAdministratorsExceptId(Integer.parseInt(id))));
    }

    /**
     * Deletes a user. If the season has started, the user's status is changed instead of deleting.
     *
     * @param id The ID of the user to delete.
     * @return HTTP response indicating whether the deletion was successful.
     */
    @PreAuthorize(("hasAnyRole('ADMINISTRATOR')"))
    @DeleteMapping("/user/{id}")
    public ResponseEntity<ResponseWrapper<Boolean>> deleteUser(@PathVariable Integer id) {
        if (this.seasonService.seasonStarted()) {
            if (this.userService.changeStatusUser(id)) {
                return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, true));
            } else {
                return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.UNABLE_TO_DELETE, false));
            }
        }
        if (this.userService.deleteUser(id)) {
            return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, true));
        } else {
            return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.UNABLE_TO_DELETE, false));
        }
    }

    /**
     * Retrieves user data for the authenticated user.
     *
     * @param id The ID of the user.
     * @return HTTP response with the user's data.
     */
    @GetMapping("/{id}/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseWrapper<User>> getMyUserData(@PathVariable int id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, user));
    }

    /**
     * Uploads a profile picture for the specified user.
     *
     * @param id    The ID of the user.
     * @param image The image file to upload as the profile picture.
     * @return HTTP response indicating the success of the upload.
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/uploadProfilePicture")
    public ResponseEntity<String> uploadProfilePicture(@PathVariable int id, @RequestParam("image") MultipartFile image) {
        try {
            User user = userService.findById(id);
            user.setProfilePicture(image.getBytes());
            userService.save(user);
            return ResponseEntity.ok("Profile picture uploaded successfully");
        } catch(IOException e) {
            return ResponseEntity.internalServerError().body("Error uploading profile picture");
        }
    }

    /**
     * Retrieves the profile picture of the specified user.
     *
     * @param id The ID of the user.
     * @return HTTP response with the user's profile picture as a byte array.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/profilePicture")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable int id) {
        User user = userService.findById(id);
        if (user == null || user.getProfilePicture() == null) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(user.getProfilePicture(), headers, HttpStatus.OK);
    }

    /**
     * Updates the user's information.
     *
     * @param id            The ID of the user.
     * @param updateUserDto The updated user information.
     * @return HTTP response with the updated user's data.
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/updateUser")
    public ResponseEntity<ResponseWrapper<User>> updateUser(@PathVariable int id, @Valid @RequestBody UpdateUserDto updateUserDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (!userDetails.getUsername().equals(updateUserDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User registeredUser = userService.findById(id);
        registeredUser.setFirstName(updateUserDto.getFirstName());
        registeredUser.setLastName(updateUserDto.getLastName());
        registeredUser.setEmail(updateUserDto.getEmail());
        registeredUser.setPhoneNumber(updateUserDto.getPhoneNumber());
        registeredUser.setBirthday(updateUserDto.getBirthday());
        registeredUser.setAddress(updateUserDto.getAddress());
        registeredUser.setIdCard(updateUserDto.getIdCard());
        registeredUser.setPartner(updateUserDto.isPartner());
        registeredUser = userService.save(registeredUser);
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, registeredUser));
    }

}
