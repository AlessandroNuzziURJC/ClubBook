package clubbook.backend.responses;

import clubbook.backend.dtos.AttendanceDto;

public class AttendanceResponse {

    private String message;
    private AttendanceDto attendanceDto;

    public AttendanceResponse(String message, AttendanceDto attendanceDto) {
        this.message = message;
        this.attendanceDto = attendanceDto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AttendanceDto getAttendanceDto() {
        return attendanceDto;
    }

    public void setAttendanceDto(AttendanceDto attendanceDto) {
        this.attendanceDto = attendanceDto;
    }
}
