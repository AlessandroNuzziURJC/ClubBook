package clubbook.backend.model.notification;

import clubbook.backend.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class NewEventNotificationFactory extends NotificationFactory {

    private static final String TITLE = "Nuevo evento";
    private static final String CONTENT_PART_1 = "¡El próximo día ";
    private static final String CONTENT_PART_2 = " hay un nuevo evento! Para más información consulte la pestaña de eventos. No olvide inscribirse al evento si desea acudir.";

    public NewEventNotificationFactory(LocalDate date, User user) {
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
