package clubbook.backend.model.notification;

import clubbook.backend.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Factory class for creating notifications when the registration deadline for an event is approaching.
 * This class constructs a notification to inform users that the registration period for a specific event
 * is about to end, encouraging them to sign up.
 */
public class EventInscriptionLimitNotificationFactory extends NotificationFactory{

    private static final String TITLE = "¡Fin de inscripción!";
    private static final String CONTENT_PART_1 = "¡Está a punto de finalizar el plazo de inscripción para el evento del día ";
    private static final String CONTENT_PART_2 = "! ¡Inscríbete!";

    /**
     * Constructs an EventInscriptionLimitNotificationFactory with the specified date and user.
     *
     * @param date the date of the event for which the registration deadline is approaching
     * @param user the user to notify about the impending end of event registration
     */
    public EventInscriptionLimitNotificationFactory(LocalDate date, User user) {
        super.notification = new Notification();
        super.notification.setTitle(TITLE);
        super.notification.setDate(date);
        super.notification.setCreatedAt(LocalDateTime.now());
        super.notification.setUser(user);
    }

    /**
     * Creates the content of the event registration limit notification.
     * This method sets the content of the notification, informing the user that the registration
     * deadline for the specified event is approaching and encourages them to register.
     */
    public void createNotification() {
        super.notification.setContent(CONTENT_PART_1 +
                + super.notification.getDate().getDayOfMonth() + " de "
                + super.getSpanishMonth(super.notification.getDate().getMonthValue()) + " " +
                + super.notification.getDate().getYear() + CONTENT_PART_2
        );
    }
}
