package clubbook.backend.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * Data Transfer Object for registering a class group.
 * It includes the group's name, address, associated teachers, and schedules.
 */
public class RegisterClassGroupDto {

    /**
     * The name of the class group. This field is mandatory.
     */
    @NotBlank(message = "Name is mandatory")
    private String name;

    /**
     * The address of the class group. This field is mandatory.
     */
    @NotBlank(message = "Address is mandatory")
    private String address;

    /**
     * A list of IDs representing the teachers associated with the class group.
     */
    private List<Integer> teachers;

    /**
     * A list of schedules associated with the class group.
     */
    private List<ScheduleDto> schedules;

    /**
     * Constructs a RegisterClassGroupDto with the specified name, address, teachers, and schedules.
     *
     * @param name the name of the class group
     * @param address the address of the class group
     * @param teachers the list of IDs representing associated teachers
     * @param schedules the list of schedules associated with the class group
     */
    public RegisterClassGroupDto(String name, String address, List<Integer> teachers, List<ScheduleDto> schedules) {
        this.name = name;
        this.address = address;
        this.teachers = teachers;
        this.schedules = schedules;
    }

    /**
     * Default constructor for creating an empty RegisterClassGroupDto.
     */
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
