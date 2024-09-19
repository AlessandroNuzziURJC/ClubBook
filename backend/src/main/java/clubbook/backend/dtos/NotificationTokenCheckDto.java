package clubbook.backend.dtos;

public class NotificationTokenCheckDto {

    private String deviceIdentifier;

    private int userId;

    public NotificationTokenCheckDto(String deviceIdentifier, int userId) {
        this.deviceIdentifier = deviceIdentifier;
        this.userId = userId;
    }

    public NotificationTokenCheckDto() {}

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
