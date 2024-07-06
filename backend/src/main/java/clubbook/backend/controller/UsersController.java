package clubbook.backend.controller;

import clubbook.backend.model.User;
import clubbook.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/me/{id}")
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
}
