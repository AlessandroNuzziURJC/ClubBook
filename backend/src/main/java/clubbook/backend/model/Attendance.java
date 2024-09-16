package clubbook.backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "T_ATTENDANCE",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"attendance_date", "user_id"})})
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate attendanceDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private boolean attended;

    public Attendance(int id, LocalDate attendanceDate, User user, boolean attended) {
        this.id = id;
        this.attendanceDate = attendanceDate;
        this.user = user;
        this.attended = attended;
    }

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
