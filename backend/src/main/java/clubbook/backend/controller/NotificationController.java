package clubbook.backend.controller;

import clubbook.backend.dtos.NotificationTokenCheckDto;
import clubbook.backend.model.NotificationToken;
import clubbook.backend.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Validated
@RequestMapping("/notification")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/token")
    public ResponseEntity<Boolean> existToken(NotificationTokenCheckDto notificationTokenCheckDto) {
        return ResponseEntity.ok( this.notificationService.find(notificationTokenCheckDto));
    }

    @PostMapping("/token")
    public ResponseEntity<String> postToken(@Valid @RequestBody NotificationToken notification) {
        return null;
    }
}
