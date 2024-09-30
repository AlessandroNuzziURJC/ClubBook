package clubbook.backend.repository;

import clubbook.backend.model.Notification;
import clubbook.backend.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByUserIdOrderByDateDesc(int userId);

    @Transactional
    @Modifying
    void deleteByDateBefore(LocalDate localDate);
}
