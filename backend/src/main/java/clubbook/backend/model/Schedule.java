package clubbook.backend.model;

import clubbook.backend.model.enumClasses.WeekDayEnum;
import jakarta.persistence.*;

import java.time.LocalTime;

/**
 * Represents a schedule entry for a class or event within the application.
 */
@Entity
@Table(name = "T_Schedule")
public class Schedule {

    /**
     * The unique identifier of a schedule.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Weekday value.
     */
    @Column(name = "weekday")
    @Enumerated(EnumType.STRING)
    private WeekDayEnum weekDay;

    /**
     * Time the class starts.
     */
    @Column(name = "init")
    private LocalTime init;

    /**
     * Class duration.
     */
    @Column(name= "duration")
    private int duration;

    /**
     * Default constructor for Schedule.
     */
    public Schedule() {}

    /**
     * Constructs a new Schedule with the specified parameters.
     *
     * @param id the unique identifier of the schedule
     * @param weekDay the day of the week for the schedule
     * @param init the starting time of the schedule
     * @param duration the duration of the schedule in minutes
     */
    public Schedule(Integer id, WeekDayEnum weekDay, LocalTime init, int duration) {
        this.id = id;
        this.weekDay = weekDay;
        this.init = init;
        this.duration = duration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public WeekDayEnum getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(WeekDayEnum weekDay) {
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
