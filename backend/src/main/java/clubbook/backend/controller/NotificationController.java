package clubbook.backend.controller;

import clubbook.backend.dtos.NotificationTokenCheckDto;
import clubbook.backend.model.NotificationToken;
import clubbook.backend.service.NotificationTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RequestMapping("/notification")
@RestController
public class NotificationController {

    private final NotificationTokenService notificationTokenService;

    @Autowired
    public NotificationController(NotificationTokenService notificationTokenService) {
        this.notificationTokenService = notificationTokenService;
    }

    @GetMapping("/token")
    public ResponseEntity<Boolean> existToken(NotificationTokenCheckDto notificationTokenCheckDto) {
        return ResponseEntity.ok( null/*this.notificationTokenService.find(notificationTokenCheckDto)*/);
    }

    @PostMapping("/token")
    public ResponseEntity<String> postToken(@Valid @RequestBody NotificationToken notification) {
        return null;
    }
}
