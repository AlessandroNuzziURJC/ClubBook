package clubbook.backend.dtos;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalTime;

/**
 * Data Transfer Object for scheduling classes or events.
 * It contains the details of the schedule including the weekday, starting time, and duration.
 */
public class ScheduleDto {

    /**
     * The unique identifier for the schedule entry.
     */
    private int id;

    /**
     * The day of the week for the scheduled event. This field is mandatory.
     */
    @NotBlank(message = "Weekday is mandatory")
    private String weekDay;

    /**
     * The starting time of the scheduled event. This field is mandatory.
     */
    @NotBlank(message = "Init is mandatory")
    private LocalTime init;

    /**
     * The duration of the scheduled event in minutes. This field is mandatory.
     */
    @NotBlank(message = "Duration is mandatory")
    private int duration;

    /**
     * Constructs a ScheduleDto with the specified schedule details.
     *
     * @param id        the unique identifier for the schedule entry
     * @param weekDay   the day of the week for the scheduled event
     * @param init      the starting time of the scheduled event
     * @param duration  the duration of the scheduled event in minutes
     */
    public ScheduleDto(int id, String weekDay, LocalTime init, int duration) {
        this.id = id;
        this.weekDay = weekDay;
        this.init = init;
        this.duration = duration;
    }

    /**
     * Default constructor for creating an empty ScheduleDto.
     */
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
