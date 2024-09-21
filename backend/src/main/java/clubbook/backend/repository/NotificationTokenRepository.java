package clubbook.backend.repository;

import clubbook.backend.model.NotificationToken;
import clubbook.backend.model.NotificationTokenId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTokenRepository extends JpaRepository<NotificationToken, NotificationTokenId> {
}

