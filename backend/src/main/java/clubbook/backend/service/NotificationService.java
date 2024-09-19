package clubbook.backend.service;

import clubbook.backend.dtos.NotificationTokenCheckDto;
import clubbook.backend.model.NotificationToken;
import clubbook.backend.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public boolean find(NotificationTokenCheckDto notificationTokenCheckDto) {
        return this.notificationRepository.findByUserIdAndDeviceIdentifier(notificationTokenCheckDto.getUserId(), notificationTokenCheckDto.getDeviceIdentifier()) != null;
    }
}
