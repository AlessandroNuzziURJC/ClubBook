package clubbook.backend.service;

import clubbook.backend.model.notification.NotificationToken;
import clubbook.backend.model.notification.NotificationTokenId;
import clubbook.backend.repository.NotificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing notification tokens within the application.
 * This class provides methods to save notification tokens and check their existence.
 */
@Service
public class NotificationTokenService {

    private final NotificationTokenRepository notificationRepository;

    /**
     * Constructs a new NotificationTokenService with the specified NotificationTokenRepository.
     *
     * @param notificationRepository the repository used for notification token operations
     */
    @Autowired
    public NotificationTokenService(NotificationTokenRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Checks if a notification token exists for a given user ID.
     *
     * @param id the ID of the user
     * @param notificationToken the notification token to check for existence
     * @return true if the notification token exists for the specified user ID, false otherwise
     */
    public Boolean find(int id, String notificationToken) {
        return this.notificationRepository.existsById(new NotificationTokenId(notificationToken, id));
    }

    /**
     * Saves a new notification token in the database.
     *
     * @param notificationTokenid the ID of the notification token to be saved
     * @return the saved NotificationToken entity
     */
    public NotificationToken save(NotificationTokenId notificationTokenid) {
        return this.notificationRepository.save(new NotificationToken(notificationTokenid));
    }
}
