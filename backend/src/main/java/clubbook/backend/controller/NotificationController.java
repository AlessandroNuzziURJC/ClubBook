package clubbook.backend.controller;

import clubbook.backend.model.notification.Notification;
import clubbook.backend.model.notification.NotificationToken;
import clubbook.backend.model.notification.NotificationTokenId;
import clubbook.backend.service.NotificationService;
import clubbook.backend.service.NotificationTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Validated
@RequestMapping("/notification")
@RestController
public class NotificationController {

    private final NotificationTokenService notificationTokenService;
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationTokenService notificationTokenService, NotificationService notificationService) {
        this.notificationTokenService = notificationTokenService;
        this.notificationService = notificationService;
    }

    @GetMapping("/token/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> existToken(@PathVariable int id, @RequestParam String notificationToken) {
        boolean exists = this.notificationTokenService.find(id, notificationToken);
        if (!exists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/token")
    public ResponseEntity<Boolean> postToken(@RequestBody @Valid NotificationTokenId notificationTokenid) {
        NotificationToken savedNotificationToken = this.notificationTokenService.save(notificationTokenid);
        return ResponseEntity.ok(savedNotificationToken != null);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable int id) {
        return ResponseEntity.ok(this.notificationService.findByUserId(id));
    }

}
