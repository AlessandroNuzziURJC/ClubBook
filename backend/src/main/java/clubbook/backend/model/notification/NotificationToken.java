package clubbook.backend.model.notification;

import jakarta.persistence.*;

/**
 * Entity class representing a notification token.
 * This class is mapped to the "T_NotificationToken" table in the database.
 */
@Entity
@Table(name = "T_NotificationToken")
public class NotificationToken {

    @EmbeddedId
    private NotificationTokenId id;

    /**
     * Default constructor for NotificationToken.
     */
    public NotificationToken() {}

    /**
     * Constructor for NotificationToken with a specified ID.
     *
     * @param id the composite identifier for the notification token
     */
    public NotificationToken(NotificationTokenId id) {
        this.id = id;
    }

    // Getters y Setters
    public NotificationTokenId getId() {
        return id;
    }

    public void setId(NotificationTokenId id) {
        this.id = id;
    }
}
