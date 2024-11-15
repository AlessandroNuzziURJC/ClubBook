package clubbook.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * Represents the attendance of a user at a specific event.
 */
@Entity
@Table(name = "T_Event_Attendance")
public class EventAttendance {

    /**
     * Unique identifier of event attendance.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The event associated with this attendance.
     */
    @JsonIgnore
    @ManyToOne
    private Event event;

    /**
     * The user associated with this attendance.
     */
    @ManyToOne
    private User user;

    /**
     * The attendance status (e.g., present, absent).
     */
    private Boolean status;

    /**
     * Default constructor for EventAttendance.
     */
    public EventAttendance() {}

    /**
     * Constructs a new EventAttendance with specified parameters.
     *
     * @param event  the event associated with this attendance
     * @param user   the user associated with this attendance
     * @param status the attendance status (e.g., present, absent)
     */
    public EventAttendance(Event event, User user, Boolean status) {
        this.event = event;
        this.user = user;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
