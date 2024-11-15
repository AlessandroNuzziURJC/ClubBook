package clubbook.backend.model.notification;

import clubbook.backend.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Factory class for creating reminders for upcoming events.
 * This class constructs a notification to remind users about an event occurring on a specific date.
 */
public class EventReminderNotificationFactory extends NotificationFactory{

    private static final String TITLE = "¡Recuerda!";
    private static final String CONTENT_PART_1 = "¡El próximo día ";
    private static final String CONTENT_PART_2 = " hay un evento! Para más información consulte la pestaña de eventos.";

    /**
     * Constructs an EventReminderNotificationFactory with the specified date and user.
     *
     * @param date the date of the upcoming event
     * @param user the user to notify about the upcoming event
     */
    public EventReminderNotificationFactory(LocalDate date, User user) {
        super.notification = new Notification();
        super.notification.setTitle(TITLE);
        super.notification.setDate(date);
        super.notification.setCreatedAt(LocalDateTime.now());
        super.notification.setUser(user);
    }

    /**
     * Creates the content of the event reminder notification.
     * This method sets the content of the notification, reminding the user about the upcoming event
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
