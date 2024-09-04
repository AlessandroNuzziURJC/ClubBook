package clubbook.backend.dtos;

import java.time.LocalDate;
import java.util.List;

public class ClassGroupAttendanceDto {

    private List<LocalDate> datesList;
    private List<UserAttendanceDto> usersList;

    public ClassGroupAttendanceDto(List<LocalDate> datesList, List<UserAttendanceDto> usersList) {
        this.datesList = datesList;
        this.usersList = usersList;
    }

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
