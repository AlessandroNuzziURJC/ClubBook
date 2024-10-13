package clubbook.backend.model.notification;

import clubbook.backend.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ModifyEventNotificationFactory extends NotificationFactory{

    private static final String TITLE = "Evento modificado";
    private static final String CONTENT_PART_1 = "Ha habido una actualización en la información del evento del día ";
    private static final String CONTENT_PART_2 = ". No olvide consultar la nueva información en la pestaña de eventos.";

    public ModifyEventNotificationFactory(LocalDate date, User user) {
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
