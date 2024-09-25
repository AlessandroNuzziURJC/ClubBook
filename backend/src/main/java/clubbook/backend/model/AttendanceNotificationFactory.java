package clubbook.backend.model;

import java.time.LocalDate;

public class AttendanceNotificationFactory extends NotificationFactory {

    private static final String TITLE = "Falta de asistencia";
    private static final String CONTENT_PART_1 = "El alumno ";
    private static final String CONTENT_PART_2 = " no ha asistido a la clase el día ";

    public AttendanceNotificationFactory(LocalDate date, User user) {
        super.notification = new Notification();
        super.notification.setTitle(TITLE);
        super.notification.setDate(date);
        super.notification.setUser(user);
    }

    public void createNotification() {
        super.notification.setContent(CONTENT_PART_1 + super.notification.getUser().getFirstName() + " "
                + super.notification.getUser().getLastName() + CONTENT_PART_2
                + super.notification.getDate().getDayOfMonth() + " de "
                + getSpanishMonth(super.notification.getDate().getMonthValue()) + " " + super.notification.getDate().getYear());
    }

    private String getSpanishMonth(int month) {
        return switch (month) {
            case 1 -> "enero";
            case 2 -> "febrero";
            case 3 -> "marzo";
            case 4 -> "abril";
            case 5 -> "mayo";
            case 6 -> "junio";
            case 7 -> "julio";
            case 8 -> "agosto";
            case 9 -> "septiembre";
            case 10 -> "octubre";
            case 11 -> "noviembre";
            case 12 -> "diciembre";
            default -> "";
        };
    }
}
