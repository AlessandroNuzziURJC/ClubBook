package clubbook.backend.dtos;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object representing attendance information for a class group
 * over multiple dates. Contains a list of dates and a list of users with their
 * attendance records.
 */
public class ClassGroupAttendanceDto {

    /**
     * List of dates for which attendance is being recorded.
     */
    private List<LocalDate> datesList;

    /**
     * List of users with their attendance details for each date.
     */
    private List<UserAttendanceDto> usersList;

    /**
     * Constructs a ClassGroupAttendanceDto with the specified dates list and users list.
     *
     * @param datesList the list of dates for attendance records
     * @param usersList the list of users with attendance details
     */
    public ClassGroupAttendanceDto(List<LocalDate> datesList, List<UserAttendanceDto> usersList) {
        this.datesList = datesList;
        this.usersList = usersList;
    }

    /**
     * Default constructor for ClassGroupAttendanceDto.
     */
    public ClassGroupAttendanceDto() {}

    public List<LocalDate> getDatesList() {
        return datesList;
    }

    public void setDatesList(List<LocalDate> datesList) {
        this.datesList = datesList;
    }

    public List<UserAttendanceDto> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<UserAttendanceDto> usersList) {
        this.usersList = usersList;
    }
}
