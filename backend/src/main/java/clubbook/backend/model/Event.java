package clubbook.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "T_Event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String additionalInfo;
    private String address;
    @ManyToOne
    private EventType type;
    private LocalDate date;
    private LocalDate birthYearStart;
    private LocalDate birthYearEnd;

    @JsonIgnore
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventAttendance> attendances = new ArrayList<>();


    public Event(int id, String title, String additionalInfo, String address, EventType type, LocalDate date, LocalDate birthYearStart, LocalDate birthYearEnd, List<EventAttendance> attendances) {
        this.id = id;
        this.title = title;
        this.additionalInfo = additionalInfo;
        this.address = address;
        this.type = type;
        this.date = date;
        this.birthYearStart = birthYearStart;
        this.birthYearEnd = birthYearEnd;
        this.attendances = attendances;
    }

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

    public List<EventAttendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<EventAttendance> attendances) {
        this.attendances = attendances;
    }
}
