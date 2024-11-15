package clubbook.backend.dtos;

/**
 * Data Transfer Object for updating event attendance information.
 * This class is used to carry the data required to update a user's attendance status for a specific event.
 */
public class UpdateEventAttendanceDto {

    /**
     * The unique identifier for the event attendance record.
     */
    private int eventAttendanceId;

    /**
     * The unique identifier for the event.
     */
    private int eventId;

    /**
     * The unique identifier for the user.
     */
    private int userId;

    /**
     * The attendance status of the user for the event.
     * This can be true (attending), false (not attending), or null (not specified).
     */
    private Boolean status;

    /**
     * Constructs an UpdateEventAttendanceDto with the specified attendance details.
     *
     * @param eventAttendanceId the unique identifier for the event attendance record
     * @param eventId          the unique identifier for the event
     * @param userId           the unique identifier for the user
     * @param status           the attendance status of the user for the event
     */
    public UpdateEventAttendanceDto(int eventAttendanceId, int eventId, int userId, Boolean status) {
        this.eventAttendanceId = eventAttendanceId;
        this.eventId = eventId;
        this.userId = userId;
        this.status = status;
    }

    /**
     * Default constructor for creating an empty UpdateEventAttendanceDto.
     */
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
