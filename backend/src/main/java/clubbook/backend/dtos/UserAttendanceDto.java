package clubbook.backend.dtos;

import java.util.List;

public class UserAttendanceDto {

    private int id;
    private String firstName;
    private String lastName;
    private List<Boolean> attendedList;

    public UserAttendanceDto(int id, String firstName, String lastName, List<Boolean> attendedList) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.attendedList = attendedList;
    }

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
