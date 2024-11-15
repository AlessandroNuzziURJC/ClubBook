package clubbook.backend.model.notification;

import clubbook.backend.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Factory class for creating attendance notifications for users.
 * This class constructs a notification when a user has missed a class.
 */
public class AttendanceNotificationFactory extends NotificationFactory {

    private static final String TITLE = "Falta de asistencia";
    private static final String CONTENT_PART_1 = "El alumno ";
    private static final String CONTENT_PART_2 = " no ha asistido a la clase el día ";

    /**
     * Constructs an AttendanceNotificationFactory with the specified date and user.
     *
     * @param date the date of the missed class
     * @param user the user who missed the class
     */
    public AttendanceNotificationFactory(LocalDate date, User user) {
        super.notification = new Notification();
        super.notification.setTitle(TITLE);
        super.notification.setDate(date);
        super.notification.setCreatedAt(LocalDateTime.now());
        super.notification.setUser(user);
    }

    /**
     * Creates the content of the attendance notification.
     * This method sets the content of the notification, detailing the user's absence
     * for the specified date.
     */
    public void createNotification() {
        super.notification.setContent(CONTENT_PART_1 + super.notification.getUser().getFirstName() + " "
                + super.notification.getUser().getLastName() + CONTENT_PART_2
                + super.notification.getDate().getDayOfMonth() + " de "
                + super.getSpanishMonth(super.notification.getDate().getMonthValue()) + " " + super.notification.getDate().getYear());
    }

}
