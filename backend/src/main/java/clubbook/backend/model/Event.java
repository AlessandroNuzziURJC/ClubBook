package clubbook.backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;

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

    public Event(int id, String title, String additionalInfo, String address, EventType type, LocalDate date, LocalDate birthYearStart, LocalDate birthYearEnd) {
        this.id = id;
        this.title = title;
        this.additionalInfo = additionalInfo;
        this.address = address;
        this.type = type;
        this.date = date;
        this.birthYearStart = birthYearStart;
        this.birthYearEnd = birthYearEnd;
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
}
