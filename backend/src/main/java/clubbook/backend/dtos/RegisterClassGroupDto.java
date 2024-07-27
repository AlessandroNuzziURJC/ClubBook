package clubbook.backend.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class RegisterClassGroupDto {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Address is mandatory")
    private String address;

    private List<Integer> teachers;

    private List<ScheduleDto> schedules;

    public RegisterClassGroupDto(String name, String address, List<Integer> teachers, List<ScheduleDto> schedules) {
        this.name = name;
        this.address = address;
        this.teachers = teachers;
        this.schedules = schedules;
    }

    public RegisterClassGroupDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Integer> getTeachers() {
        return teachers;
    }

    public void setIdTeachers(List<Integer> teachers) {
        this.teachers = teachers;
    }

    public List<ScheduleDto> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<ScheduleDto> schedules) {
        this.schedules = schedules;
    }
}
