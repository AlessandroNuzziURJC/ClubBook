package clubbook.backend.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class RegisterClassGroupDto {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Address is mandatory")
    private String address;

    private List<Integer> idTeachers;

    private List<ScheduleDto> schedulesDto;

    public RegisterClassGroupDto(String name, String address, List<Integer> idTeachers, List<ScheduleDto> schedulesDto) {
        this.name = name;
        this.address = address;
        this.idTeachers = idTeachers;
        this.schedulesDto = schedulesDto;
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

    public List<Integer> getIdTeachers() {
        return idTeachers;
    }

    public void setIdTeachers(List<Integer> idTeachers) {
        this.idTeachers = idTeachers;
    }

    public List<ScheduleDto> getSchedulesDto() {
        return schedulesDto;
    }

    public void setSchedulesDto(List<ScheduleDto> schedulesDto) {
        this.schedulesDto = schedulesDto;
    }
}
