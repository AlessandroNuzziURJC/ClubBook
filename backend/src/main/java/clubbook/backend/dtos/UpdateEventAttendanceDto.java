package clubbook.backend.dtos;

public class UpdateEventAttendanceDto {

    private int eventAttendanceId;
    private int eventId;
    private int userId;
    private Boolean status;

    public UpdateEventAttendanceDto(int eventAttendanceId, int eventId, int userId, Boolean status) {
        this.eventAttendanceId = eventAttendanceId;
        this.eventId = eventId;
        this.userId = userId;
        this.status = status;
    }

    public UpdateEventAttendanceDto() {
    }

    public int getEventAttendanceId() {
        return eventAttendanceId;
    }

    public void setEventAttendanceId(int eventAttendanceId) {
        this.eventAttendanceId = eventAttendanceId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
