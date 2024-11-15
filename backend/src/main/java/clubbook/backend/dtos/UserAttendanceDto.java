package clubbook.backend.dtos;

import java.util.List;

/**
 * Data Transfer Object representing a user's attendance details.
 * This class is used to carry the information about a user's
 * attendance along with their personal details.
 */
public class UserAttendanceDto {

    /**
     * The unique identifier for the user.
     */
    private int id;

    /**
     * The first name of the user.
     */
    private String firstName;

    /**
     * The last name of the user.
     */
    private String lastName;

    /**
     * A list indicating the attendance status for events.
     * Each entry corresponds to an event, with true indicating attendance
     * and false indicating absence.
     */
    private List<Boolean> attendedList;

    /**
     * Constructs a new UserAttendanceDto with the specified parameters.
     *
     * @param id the unique identifier for the user
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param attendedList a list indicating the user's attendance status for events
     */
    public UserAttendanceDto(int id, String firstName, String lastName, List<Boolean> attendedList) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.attendedList = attendedList;
    }

    /**
     * Default constructor for creating an empty UserAttendanceDto.
     */
    public UserAttendanceDto() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Boolean> getAttendanceList() {
        return attendedList;
    }

    public void setAttendanceList(List<Boolean> attendedList) {
        this.attendedList = attendedList;
    }
}
