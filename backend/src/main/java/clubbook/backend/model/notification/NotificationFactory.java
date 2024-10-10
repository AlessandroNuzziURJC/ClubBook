package clubbook.backend.model.notification;

import java.time.LocalDate;

public abstract class NotificationFactory {

    protected String title;
    protected LocalDate date;
    protected Notification notification;

    public abstract void createNotification();

    public Notification getNotification() {
        return notification;
    }

    protected String getSpanishMonth(int month) {
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
