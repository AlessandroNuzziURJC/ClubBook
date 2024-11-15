package clubbook.backend.repository;

import clubbook.backend.model.notification.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for managing Notification entities.
 * This interface extends JpaRepository to provide CRUD operations
 * for Notification entities and additional custom query methods.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    /**
     * Retrieves a list of notifications for a specific user, ordered by creation date in descending order.
     *
     * @param userId the ID of the user for whom notifications are to be retrieved.
     * @return a list of notifications ordered by their creation date.
     */
    List<Notification> findByUserIdOrderByCreatedAtDesc(int userId);

    /**
     * Deletes all notifications that were created before a specified date.
     * This method is transactional and modifies the state of the database.
     *
     * @param localDate the date before which notifications will be deleted.
     */
    @Transactional
    @Modifying
    void deleteByCreatedAtBefore(LocalDate localDate);
}
