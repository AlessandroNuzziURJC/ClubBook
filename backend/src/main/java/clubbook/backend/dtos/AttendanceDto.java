package clubbook.backend.dtos;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object representing the attendance details for a specific class session.
 * Contains information about the date, class group, and lists of users who attended
 * or did not attend the session.
 */
public class AttendanceDto {

    /**
     * The date of the attendance record.
     */
    private LocalDate date;

    /**
     * Identifier for the class group.
     */
    private int classGroup;

    /**
     * List of user IDs who attended the class.
     */
    private List<Integer> usersIdsAttended;

    /**
     * List of user IDs who did not attend the class.
     */
    private List<Integer> usersIdsNotAttended;

    /**
     * Constructs an AttendanceDto with the specified date, class group, and attendance lists.
     *
     * @param date the date of the attendance record
     * @param classGroup the identifier of the class group
     * @param usersIdsAttended the list of user IDs who attended
     * @param usersIdsNotAttended the list of user IDs who did not attend
     */
    public AttendanceDto(LocalDate date, int classGroup, List<Integer> usersIdsAttended, List<Integer> usersIdsNotAttended) {
        this.date = date;
        this.classGroup = classGroup;
        this.usersIdsAttended = usersIdsAttended;
        this.usersIdsNotAttended = usersIdsNotAttended;
    }


    /**
     * Default constructor for AttendanceDto.
     */
    public AttendanceDto() {}

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(int classGroup) {
        this.classGroup = classGroup;
    }

    public List<Integer> getUsersIdsAttended() {
        return usersIdsAttended;
    }

    public void setUsersIdsAttended(List<Integer> usersIdsAttended) {
        this.usersIdsAttended = usersIdsAttended;
    }

    public List<Integer> getUsersIdsNotAttended() {
        return usersIdsNotAttended;
    }

    public void setUsersIdsNotAttended(List<Integer> usersIdsNotAttended) {
        this.usersIdsNotAttended = usersIdsNotAttended;
    }
}
