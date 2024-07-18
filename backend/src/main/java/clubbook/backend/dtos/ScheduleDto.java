package clubbook.backend.dtos;

import clubbook.backend.model.WeekDayEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalTime;

public class ScheduleDto {

    @NotBlank(message = "Weekday is mandatory")
    private String weekDay;

    @NotBlank(message = "Init is mandatory")
    private LocalTime init;

    @NotBlank(message = "Finish is mandatory")
    private LocalTime finish;

    public ScheduleDto(String weekDay, LocalTime init, LocalTime finish) {
        this.weekDay = weekDay;
        this.init = init;
        this.finish = finish;
    }

    public ScheduleDto() {
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public LocalTime getInit() {
        return init;
    }

    public void setInit(LocalTime init) {
        this.init = init;
    }

    public LocalTime getFinish() {
        return finish;
    }

    public void setFinish(LocalTime finish) {
        this.finish = finish;
    }
}
