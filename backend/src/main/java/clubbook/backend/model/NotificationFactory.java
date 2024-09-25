package clubbook.backend.model;

import java.time.LocalDate;

public abstract class NotificationFactory {

    protected String title;
    protected LocalDate date;
    protected Notification notification;

    public abstract void createNotification();

    public Notification getNotification() {
        return notification;
    }
}
