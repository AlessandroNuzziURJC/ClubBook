package clubbook.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an event within the application, including details such as
 * title, type, date, and attendance information.
 */
@Entity
@Table(name = "T_Event")
public class Event {

    /**
     * The unique identifier of the event.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The title of the event.
     */
    private String title;

    /**
     * Additional information about the event.
     */
    private String additionalInfo;

    /**
     * The address where the event will take place.
     */
    private String address;

    /**
     * The type of the event.
     */
    @ManyToOne
    private EventType type;

    /**
     * The date of the event.
     */
    private LocalDate date;

    /**
     * The starting birth year for participants.
     */
    private LocalDate birthYearStart;

    /**
     * The ending birth year for participants
     */
    private LocalDate birthYearEnd;

    /**
     * The deadline for event registration.
     */
    private LocalDate deadline;

    /**
     * The list of attendances for this event.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventAttendance> attendances = new ArrayList<>();

    /**
     * Constructs a new Event with specified parameters.
     *
     * @param id              the unique identifier of the event
     * @param title           the title of the event
     * @param additionalInfo  additional information about the event
     * @param address         the address where the event will take place
     * @param type            the type of the event
     * @param date            the date of the event
     * @param birthYearStart  the starting birth year for participants
     * @param birthYearEnd    the ending birth year for participants
     * @param deadline        the deadline for event registration
     * @param attendances     the list of attendances for this event
     */
    public Event(int id, String title, String additionalInfo, String address, EventType type, LocalDate date, LocalDate birthYearStart, LocalDate birthYearEnd, LocalDate deadline, List<EventAttendance> attendances) {
        this.id = id;
        this.title = title;
        this.additionalInfo = additionalInfo;
        this.address = address;
        this.type = type;
        this.date = date;
        this.birthYearStart = birthYearStart;
        this.birthYearEnd = birthYearEnd;
        this.deadline = deadline;
        this.attendances = attendances;
    }

    /**
     * Default constructor for Event.
     */
    public Event() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getBirthYearStart() {
        return birthYearStart;
    }

    public void setBirthYearStart(LocalDate birthYearStart) {
        this.birthYearStart = birthYearStart;
    }

    public LocalDate getBirthYearEnd() {
        return birthYearEnd;
    }

    public void setBirthYearEnd(LocalDate birthYearEnd) {
        this.birthYearEnd = birthYearEnd;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public List<EventAttendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<EventAttendance> attendances) {
        this.attendances = attendances;
    }
}
