package clubbook.backend.repository;

import clubbook.backend.model.notification.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(int userId);

    @Transactional
    @Modifying
    void deleteByCreatedAtBefore(LocalDate localDate);
}
