package clubbook.backend.model.notification;

import clubbook.backend.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EventInscriptionLimitNotificationFactory extends NotificationFactory{

    private static final String TITLE = "¡Fin de inscripción!";
    private static final String CONTENT_PART_1 = "¡Está a punto de finalizar el plazo de inscripción para el evento del día";
    private static final String CONTENT_PART_2 = "! ¡Inscríbete antes de que se agoten las plazas!";

    public EventInscriptionLimitNotificationFactory(LocalDate date, User user) {
        super.notification = new Notification();
        super.notification.setTitle(TITLE);
        super.notification.setDate(date);
        super.notification.setCreatedAt(LocalDateTime.now());
        super.notification.setUser(user);
    }

    public void createNotification() {
        super.notification.setContent(CONTENT_PART_1 +
                + super.notification.getDate().getDayOfMonth() + " de "
                + super.getSpanishMonth(super.notification.getDate().getMonthValue()) + " " +
                + super.notification.getDate().getYear() + CONTENT_PART_2
        );
    }
}
