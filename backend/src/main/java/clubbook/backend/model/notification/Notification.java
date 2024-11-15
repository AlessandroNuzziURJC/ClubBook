package clubbook.backend.model.notification;

import clubbook.backend.model.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * Entity class representing a notification in the system.
 * This class maps to the "T_Notification" table in the database and contains information about a specific notification,
 * including its title, content, date, and the user associated with it.
 */
@Entity
@Table(name = "T_Notification")
public class Notification {

    /**
     * Unique identifier of the notification.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Title of the notification.
     */
    private String title;

    /**
     * Date of creation.
     */
    private LocalDateTime createdAt;

    /**
     * Date for notifications that need date information.
     */
    private LocalDate date;

    /**
     * Information of the notification.
     */
    private String content;

    /**
     * User who is notification created for.
     */
    @ManyToOne
    private User user;

    /**
     * Default constructor for the Notification class.
     */
    public Notification() {}

    /**
     * Constructs a Notification with the specified parameters.
     *
     * @param id the unique identifier of the notification
     * @param title the title of the notification
     * @param createdAt the date and time the notification was created
     * @param date the date associated with the notification
     * @param content the content of the notification
     * @param user the user associated with the notification
     */
    public Notification(int id, String title, LocalDateTime createdAt, LocalDate date, String content, User user) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.date = date;
        this.content = content;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
