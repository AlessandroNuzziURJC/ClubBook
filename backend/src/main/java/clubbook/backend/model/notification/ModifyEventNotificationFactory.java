package clubbook.backend.model.notification;

import clubbook.backend.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Factory class for creating notifications for modified events.
 * This class constructs a notification to inform users about updates to an event's information on a specific date.
 */
public class ModifyEventNotificationFactory extends NotificationFactory{

    private static final String TITLE = "Evento modificado";
    private static final String CONTENT_PART_1 = "Ha habido una actualización en la información del evento del día ";
    private static final String CONTENT_PART_2 = ". No olvide consultar la nueva información en la pestaña de eventos.";

    /**
     * Constructs a ModifyEventNotificationFactory with the specified date and user.
     *
     * @param date the date of the modified event
     * @param user the user to notify about the event modification
     */
    public ModifyEventNotificationFactory(LocalDate date, User user) {
        super.notification = new Notification();
        super.notification.setTitle(TITLE);
        super.notification.setDate(date);
        super.notification.setCreatedAt(LocalDateTime.now());
        super.notification.setUser(user);
    }

    /**
     * Creates the content of the event modification notification.
     * This method sets the content of the notification, informing the user about the updates
     * to the event on the specified date and directing them to the events tab for more information.
     */
    public void createNotification() {
        super.notification.setContent(CONTENT_PART_1 +
                + super.notification.getDate().getDayOfMonth() + " de "
                + super.getSpanishMonth(super.notification.getDate().getMonthValue()) + " " +
                + super.notification.getDate().getYear() + CONTENT_PART_2
        );
    }
}
