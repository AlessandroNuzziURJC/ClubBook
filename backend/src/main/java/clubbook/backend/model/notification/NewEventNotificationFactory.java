package clubbook.backend.model.notification;

import clubbook.backend.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Factory class for creating notifications for new events.
 * This class constructs a notification to inform users about a new event happening on a specific date.
 */
public class NewEventNotificationFactory extends NotificationFactory {

    private static final String TITLE = "Nuevo evento";
    private static final String CONTENT_PART_1 = "¡El próximo día ";
    private static final String CONTENT_PART_2 = " hay un nuevo evento! Para más información consulte la pestaña de eventos. No olvide inscribirse al evento si desea acudir.";

    /**
     * Constructs a NewEventNotificationFactory with the specified date and user.
     *
     * @param date the date of the new event
     * @param user the user to notify about the new event
     */
    public NewEventNotificationFactory(LocalDate date, User user) {
        super.notification = new Notification();
        super.notification.setTitle(TITLE);
        super.notification.setDate(date);
        super.notification.setCreatedAt(LocalDateTime.now());
        super.notification.setUser(user);
    }

    /**
     * Creates the content of the new event notification.
     * This method sets the content of the notification, informing the user about the new event
     * on the specified date and directing them to the events tab for more information.
     */
    public void createNotification() {
        super.notification.setContent(CONTENT_PART_1 +
                + super.notification.getDate().getDayOfMonth() + " de "
                + super.getSpanishMonth(super.notification.getDate().getMonthValue()) + " " +
                + super.notification.getDate().getYear() + CONTENT_PART_2
        );
    }

}
