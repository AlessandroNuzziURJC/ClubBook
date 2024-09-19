package clubbook.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "T_NotificationToken")
public class NotificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String deviceIdentifier;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private int userId;

    public NotificationToken(int id, String deviceIdentifier, String token, int userId) {
        this.id = id;
        this.deviceIdentifier = deviceIdentifier;
        this.token = token;
        this.userId = userId;
    }

    public NotificationToken() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
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
}
