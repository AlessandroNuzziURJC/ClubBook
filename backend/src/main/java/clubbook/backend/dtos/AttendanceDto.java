package clubbook.backend.dtos;

import java.time.LocalDate;
import java.util.List;

public class AttendanceDto {

    private LocalDate date;

    private int classGroup;

    private List<Integer> usersIdsAttended;

    private List<Integer> usersIdsNotAttended;

    public AttendanceDto(LocalDate date, int classGroup, List<Integer> usersIdsAttended, List<Integer> usersIdsNotAttended) {
        this.date = date;
        this.classGroup = classGroup;
        this.usersIdsAttended = usersIdsAttended;
        this.usersIdsNotAttended = usersIdsNotAttended;
    }

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
