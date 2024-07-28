package clubbook.backend.model;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "T_Schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "weekday")
    @Enumerated(EnumType.STRING)
    private WeekDayEnum weekDay;

    @Column(name = "init")
    private LocalTime init;

    @Column(name= "duration")
    private int duration;

    public Schedule() {}

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
