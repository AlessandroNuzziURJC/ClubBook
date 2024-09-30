package clubbook.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "T_NotificationToken")
public class NotificationToken {

    @EmbeddedId
    private NotificationTokenId id;

    public NotificationToken() {}

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
