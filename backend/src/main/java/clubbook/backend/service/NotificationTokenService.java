package clubbook.backend.service;

import clubbook.backend.repository.NotificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationTokenService {

    private final NotificationTokenRepository notificationRepository;

    @Autowired
    public NotificationTokenService(NotificationTokenRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

}
