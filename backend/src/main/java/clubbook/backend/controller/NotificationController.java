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

/**
 * REST controller for managing notifications and notification tokens.
 */
@Validated
@RequestMapping("/notification")
@RestController
public class NotificationController {

    private final NotificationTokenService notificationTokenService;
    private final NotificationService notificationService;

    /**
     * Constructor to inject notification token and notification services.
     *
     * @param notificationTokenService Service for handling notification tokens.
     * @param notificationService Service for handling notifications.
     */
    @Autowired
    public NotificationController(NotificationTokenService notificationTokenService, NotificationService notificationService) {
        this.notificationTokenService = notificationTokenService;
        this.notificationService = notificationService;
    }

    /**
     * Checks if a specific notification token exists for a given user.
     *
     * @param id Identifier of the user.
     * @param notificationToken Notification token to check.
     * @return HTTP 200 response if the token exists, 404 if it does not.
     */
    @GetMapping("/token/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> existToken(@PathVariable int id, @RequestParam String notificationToken) {
        boolean exists = this.notificationTokenService.find(id, notificationToken);
        if (!exists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Saves a new notification token for a user.
     *
     * @param notificationTokenId ID of the notification token to be saved.
     * @return HTTP response with true if the token was saved successfully, false otherwise.
     */
    @PostMapping("/token")
    public ResponseEntity<Boolean> postToken(@RequestBody @Valid NotificationTokenId notificationTokenId) {
        NotificationToken savedNotificationToken = this.notificationTokenService.save(notificationTokenId);
        return ResponseEntity.ok(savedNotificationToken != null);
    }

    /**
     * Retrieves a list of notifications for a specific user.
     *
     * @param id Identifier of the user.
     * @return List of notifications for the user.
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable int id) {
        return ResponseEntity.ok(this.notificationService.findByUserId(id));
    }

}
