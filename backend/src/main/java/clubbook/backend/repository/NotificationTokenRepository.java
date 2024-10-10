package clubbook.backend.repository;

import clubbook.backend.model.notification.NotificationToken;
import clubbook.backend.model.notification.NotificationTokenId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTokenRepository extends JpaRepository<NotificationToken, NotificationTokenId> {
}

