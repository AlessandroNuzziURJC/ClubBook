package clubbook.backend.model.notification;

import clubbook.backend.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Factory class for creating notifications when event inscriptions are finished.
 * This class constructs a notification to inform users that the registration period
 * for a specific event has ended.
 */
public class EventInscriptionFinishedNotificationFactory extends NotificationFactory {

    private static final String TITLE = "¡Fin de inscripción!";
    private static final String CONTENT_PART_1 = "Ha finalizado la inscripción al evento del día ";
    private static final String CONTENT_PART_2 = ". Consulta los asistentes en la pestaña de eventos.";

    /**
     * Constructs an EventInscriptionFinishedNotificationFactory with the specified date and user.
     *
     * @param date the date of the event for which registration has ended
     * @param user the user to notify about the end of event registration
     */
    public EventInscriptionFinishedNotificationFactory(LocalDate date, User user) {
        super.notification = new Notification();
        super.notification.setTitle(TITLE);
        super.notification.setDate(date);
        super.notification.setCreatedAt(LocalDateTime.now());
        super.notification.setUser(user);
    }

    /**
     * Creates the content of the event registration finished notification.
     * This method sets the content of the notification, informing the user that the registration
     * for the specified event has ended and encourages them to check the attendees.
     */
    public void createNotification() {
        super.notification.setContent(CONTENT_PART_1 +
                + super.notification.getDate().getDayOfMonth() + " de "
                + super.getSpanishMonth(super.notification.getDate().getMonthValue()) + " " +
                + super.notification.getDate().getYear() + CONTENT_PART_2
        );
    }
}