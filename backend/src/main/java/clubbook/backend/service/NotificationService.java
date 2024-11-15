package clubbook.backend.service;

import clubbook.backend.model.notification.Notification;
import clubbook.backend.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class for managing notifications within the application.
 * This class provides methods to save, retrieve, and delete notifications.
 */
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * Constructs a new NotificationService with the specified NotificationRepository.
     *
     * @param notificationRepository the repository used for notification operations
     */
    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Saves a notification to the database.
     *
     * @param notification the notification to be saved
     * @return the saved notification
     */
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    /**
     * Retrieves a list of notifications for a specific user, ordered by creation date in descending order.
     *
     * @param userId the ID of the user whose notifications are to be retrieved
     * @return a list of notifications for the specified user
     */
    public List<Notification> findByUserId(int userId) {
        return this.notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Schedules the deletion of notifications that are older than 7 days.
     * This method is executed at midnight every day.
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleDeleteOldNotifications() {
        notificationRepository.deleteByCreatedAtBefore(LocalDate.now().minusDays(7));
    }

    /**
     * Deletes all notifications from the database.
     */
    public void deleteAll() {
        this.notificationRepository.deleteAll();
    }
}
