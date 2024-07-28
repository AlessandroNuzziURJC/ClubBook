package clubbook.backend.dtos;

import clubbook.backend.model.WeekDayEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalTime;

public class ScheduleDto {

    private int id;

    @NotBlank(message = "Weekday is mandatory")
    private String weekDay;

    @NotBlank(message = "Init is mandatory")
    private LocalTime init;

    @NotBlank(message = "Duration is mandatory")
    private int duration;

    public ScheduleDto(int id, String weekDay, LocalTime init, int duration) {
        this.id = id;
        this.weekDay = weekDay;
        this.init = init;
        this.duration = duration;
    }

    //PRobar meter el id a ver si lo actualioza, si no crear la tabla enlazarlo y gestioanrlo indpenedientement

    public ScheduleDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
