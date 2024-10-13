package clubbook.backend.model.notification;

import clubbook.backend.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AttendanceNotificationFactory extends NotificationFactory {

    private static final String TITLE = "Falta de asistencia";
    private static final String CONTENT_PART_1 = "El alumno ";
    private static final String CONTENT_PART_2 = " no ha asistido a la clase el día ";

    public AttendanceNotificationFactory(LocalDate date, User user) {
        super.notification = new Notification();
        super.notification.setTitle(TITLE);
        super.notification.setDate(date);
        super.notification.setCreatedAt(LocalDateTime.now());
        super.notification.setUser(user);
    }

    public void createNotification() {
        super.notification.setContent(CONTENT_PART_1 + super.notification.getUser().getFirstName() + " "
                + super.notification.getUser().getLastName() + CONTENT_PART_2
                + super.notification.getDate().getDayOfMonth() + " de "
                + super.getSpanishMonth(super.notification.getDate().getMonthValue()) + " " + super.notification.getDate().getYear());
    }

}
