package clubbook.backend.controller;

import clubbook.backend.model.Notification;
import clubbook.backend.model.NotificationToken;
import clubbook.backend.model.NotificationTokenId;
import clubbook.backend.service.NotificationService;
import clubbook.backend.service.NotificationTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
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
    public ResponseEntity<Boolean> existToken(@PathVariable int id, @RequestParam String notificationToken) {
        return ResponseEntity.ok( this.notificationTokenService.find(id, notificationToken));
    }

    @PostMapping("/token")
    public ResponseEntity<Boolean> postToken(@RequestBody @Valid NotificationTokenId notificationTokenid) {
        return ResponseEntity.ok(this.notificationTokenService.save(notificationTokenid) != null);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable int id) {
        return ResponseEntity.ok(this.notificationService.findByUserId(id));
    }


}
