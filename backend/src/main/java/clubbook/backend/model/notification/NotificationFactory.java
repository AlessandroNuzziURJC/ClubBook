package clubbook.backend.model.notification;

import java.time.LocalDate;

/**
 * Abstract factory class for creating notifications.
 * This class provides a template for creating different types of notifications,
 * including methods for constructing notifications and formatting month names in Spanish.
 */
public abstract class NotificationFactory {

    protected String title;
    protected LocalDate date;
    protected Notification notification;

    /**
     * Abstract method for creating a notification.
     * Subclasses must provide an implementation for this method to create a specific notification type.
     */
    public abstract void createNotification();

    /**
     * Gets the created notification.
     *
     * @return the notification created by the factory
     */
    public Notification getNotification() {
        return notification;
    }

    /**
     * Converts a month number to its corresponding name in Spanish.
     *
     * @param month the month number (1 for January, 2 for February, etc.)
     * @return the name of the month in Spanish, or an empty string if the month number is invalid
     */
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
