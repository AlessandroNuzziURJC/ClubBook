package clubbook.backend.service;

import clubbook.backend.model.Notification;
import clubbook.backend.model.User;
import clubbook.backend.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    public List<Notification> findByUserId(int userId) {
        return this.notificationRepository.findByUserIdOrderByDateDesc(userId);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleDeleteOldNotifications() {
        System.out.println("Ejecutando...");
        notificationRepository.deleteByDateBefore(LocalDate.now().minusDays(30));
    }
}
