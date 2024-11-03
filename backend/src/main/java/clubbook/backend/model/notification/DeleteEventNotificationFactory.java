package clubbook.backend.model.notification;

import clubbook.backend.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Factory class for creating notifications for canceled events.
 * This class constructs a notification informing users that a specific event has been canceled.
 */
public class DeleteEventNotificationFactory extends NotificationFactory {

    private static final String TITLE = "Evento cancelado";
    private static final String CONTENT_PART_1 = "Se ha cancelado el evento del día ";
    private static final String CONTENT_PART_2 = ". Para más información consulte al profesor.";

    /**
     * Constructs a DeleteEventNotificationFactory with the specified date and user.
     *
     * @param date the date of the canceled event
     * @param user the user to notify about the canceled event
     */
    public DeleteEventNotificationFactory(LocalDate date, User user) {
        super.notification = new Notification();
        super.notification.setTitle(TITLE);
        super.notification.setDate(date);
        super.notification.setCreatedAt(LocalDateTime.now());
        super.notification.setUser(user);
    }

    /**
     * Creates the content of the event cancellation notification.
     * This method sets the content of the notification, informing the user of the canceled event
     * on the specified date.
     */
    public void createNotification() {
        super.notification.setContent(CONTENT_PART_1 +
                + super.notification.getDate().getDayOfMonth() + " de "
                + super.getSpanishMonth(super.notification.getDate().getMonthValue()) + " " +
                + super.notification.getDate().getYear() + CONTENT_PART_2
        );
    }
}
