package clubbook.backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Class representing an attendance record for a user on a specific date.
 */
@Entity
@Table(name = "T_ATTENDANCE",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"attendance_date", "user_id"})})
public class Attendance {

    /**
     * Unique identifier of attendance.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Date when the attendance was registered.
     */
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate attendanceDate;

    /**
     * User who attendance is referring to.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Indicates if user attended or not.
     */
    @Column(nullable = false)
    private boolean attended;

    /**
     * Constructor for Attendance with specified parameters.
     *
     * @param id             the unique identifier for the attendance record
     * @param attendanceDate the date of attendance
     * @param user           the user associated with this attendance record
     * @param attended       indicates whether the user attended (true) or not (false)
     */
    public Attendance(int id, LocalDate attendanceDate, User user, boolean attended) {
        this.id = id;
        this.attendanceDate = attendanceDate;
        this.user = user;
        this.attended = attended;
    }

    /**
     * Default constructor for Attendance.
     */
    public Attendance() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }
}
