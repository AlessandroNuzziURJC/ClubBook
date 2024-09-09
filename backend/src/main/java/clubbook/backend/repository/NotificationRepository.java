package clubbook.backend.repository;

import clubbook.backend.model.NotificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationToken, Integer> {

    NotificationToken findByUserIdAndDeviceIdentifier(Integer userId, String deviceIdentifier);
}
