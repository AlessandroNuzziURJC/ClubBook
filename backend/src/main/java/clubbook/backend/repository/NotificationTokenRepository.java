package clubbook.backend.repository;

import clubbook.backend.model.notification.NotificationToken;
import clubbook.backend.model.notification.NotificationTokenId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing NotificationToken entities.
 * This interface extends JpaRepository to provide CRUD operations
 * for NotificationToken entities identified by a composite key.
 */
public interface NotificationTokenRepository extends JpaRepository<NotificationToken, NotificationTokenId> {
    // No additional methods are defined here, as the JpaRepository
    // provides the necessary CRUD functionality out of the box.
}

