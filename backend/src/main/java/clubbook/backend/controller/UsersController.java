package clubbook.backend.controller;

import clubbook.backend.dtos.RegisterUserDto;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

@RestController()
public class UsersController {

    private final UserService userService;
    private final SeasonService seasonService;

    @Autowired
    public UsersController(UserService userService, SeasonService seasonService) {
        this.userService = userService;
        this.seasonService = seasonService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<ResponseWrapper<List<User>>> getAllUsers() {
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, userService.getAllUsers()));
    }

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

    @GetMapping("/studentsWithoutClassGroup")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<ResponseWrapper<List<User>>> getAllStudentsWithoutClassGroup() {
        List<User> users = userService.getAllStudentsWithoutClassGroup();
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, users));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'TEACHER')")
    @GetMapping("/studentsSearch")
    public ResponseEntity<ResponseWrapper<List<User>>> getStudentsListFilteredByName(@RequestParam String search) {
        List<User> list = userService.getStudentsListFilteredByName(search);
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, list));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @GetMapping("/teachers")
    public ResponseEntity<ResponseWrapper<Page<User>>> getAllTeachers(@RequestParam(defaultValue = "0") int pageNumber,
                                                     @RequestParam(defaultValue = "10") int pageSize) {
        Page<User> page = userService.getTeachersPage(pageNumber, pageSize);
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, page));
    }

    @PreAuthorize(("hasAnyRole('ADMINISTRATOR')"))
    @GetMapping("/allTeachers")
    public ResponseEntity<ResponseWrapper<List<User>>> getAllTeachers() {
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, userService.getAllTeachers()));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @GetMapping("/teachersSearch")
    public ResponseEntity<ResponseWrapper<List<User>>> getTeachersListFilteredByName(@RequestParam String search) {
        List<User> list = userService.getTeachersListFilteredByName(search);
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, list));
    }

    @GetMapping("/{id}/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseWrapper<User>> getMyUserData(@PathVariable int id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(new ResponseWrapper<>(ResponseMessages.OK, user));
    }

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
