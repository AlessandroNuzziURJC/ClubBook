package clubbook.backend.controller;

import clubbook.backend.dtos.RegisterUserDto;
import clubbook.backend.dtos.UpdateUserDto;
import clubbook.backend.model.User;
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

@RestController()
public class UsersController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    //@PreAuthorize("isAuthenticated()")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/students")
    public Page<User> getAllStudents(@RequestParam(defaultValue = "0") int pageNumber,
                                     @RequestParam(defaultValue = "10") int pageSize) {
        Page<User> page =userService.getStudentsPage(pageNumber, pageSize);
        return page;
    }

    @GetMapping("/{id}/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> getMyUserData(@PathVariable int id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
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
    public ResponseEntity<User> updateUser(@PathVariable int id, @Valid @RequestBody UpdateUserDto updateUserDto) {
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
        return ResponseEntity.ok(registeredUser);
    }

}
