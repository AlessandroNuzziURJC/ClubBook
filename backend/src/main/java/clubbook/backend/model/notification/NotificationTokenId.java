package clubbook.backend.model.notification;

import jakarta.persistence.Embeddable;

import java.util.Objects;

/**
 * Class representing a composite identifier for the NotificationToken entity.
 * This class is used as an embedded ID in the NotificationToken entity.
 */
@Embeddable
public class NotificationTokenId {
    private String token;
    private int userId;

    /**
     * Default constructor for NotificationTokenId.
     */
    public NotificationTokenId() {}

    /**
     * Constructor for NotificationTokenId with specified token and user ID.
     *
     * @param token  the notification token
     * @param userId the user ID associated with the token
     */
    public NotificationTokenId(String token, int userId) {
        this.token = token;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NotificationTokenId that = (NotificationTokenId) obj;
        return Objects.equals(token, that.token) && Objects.equals(userId, that.userId);
    }
}
