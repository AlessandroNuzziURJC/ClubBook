package clubbook.backend.service;

import clubbook.backend.model.NotificationToken;
import clubbook.backend.model.NotificationTokenId;
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

    public Boolean find(int id, String notificationToken) {
        return this.notificationRepository.existsById(new NotificationTokenId(notificationToken, (long) id));
    }

    public NotificationToken save(NotificationTokenId notificationTokenid) {
        return this.notificationRepository.save(new NotificationToken(notificationTokenid));
    }
}
