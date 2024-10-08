package clubbook.backend.dtos;

import clubbook.backend.model.Event;
import clubbook.backend.model.EventType;

import java.time.LocalDate;

public class EventDto {
    private int id;

    private String title;
    private String additionalInfo;
    private String address;
    private EventType type;
    private LocalDate date;
    private LocalDate birthYearStart;
    private LocalDate birthYearEnd;

    public EventDto(int id, String title, String additionalInfo, String address, EventType type, LocalDate date, LocalDate birthYearStart, LocalDate birthYearEnd) {
        this.id = id;
        this.title = title;
        this.additionalInfo = additionalInfo;
        this.address = address;
        this.type = type;
        this.date = date;
        this.birthYearStart = birthYearStart;
        this.birthYearEnd = birthYearEnd;
    }

    public EventDto() {
    }

    public EventDto(Event event) {
        this.id= event.getId();
        this.title = event.getTitle();
        this.additionalInfo = event.getAdditionalInfo();
        this.address = event.getAddress();
        this.type = event.getType();
        this.date = event.getDate();
        this.birthYearStart = event.getBirthYearStart();
        this.birthYearEnd = event.getBirthYearEnd();
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
