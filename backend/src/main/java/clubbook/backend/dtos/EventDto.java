package clubbook.backend.dtos;

import clubbook.backend.model.Event;
import clubbook.backend.model.EventType;

import java.time.LocalDate;

/**
 * Data Transfer Object representing an event.
 * Contains details such as the event title, type, date, location, and age range.
 */
public class EventDto {

    /**
     * Unique identifier for the event.
     */
    private int id;

    /**
     * Title of the event.
     */
    private String title;

    /**
     * Additional information about the event.
     */
    private String additionalInfo;

    /**
     * Address where the event will take place.
     */
    private String address;

    /**
     * Type of the event.
     */
    private EventType type;

    /**
     * Date on which the event will be held.
     */
    private LocalDate date;

    /**
     * Start of the birth year range allowed for the event.
     */
    private LocalDate birthYearStart;

    /**
     * End of the birth year range allowed for the event.
     */
    private LocalDate birthYearEnd;

    /**
     * Deadline date for registering for the event.
     */
    private LocalDate deadline;

    /**
     * Constructs an EventDto with specified details.
     *
     * @param id the unique identifier of the event
     * @param title the title of the event
     * @param additionalInfo additional information about the event
     * @param address the address of the event location
     * @param type the type of the event
     * @param date the date of the event
     * @param birthYearStart the starting birth year for eligible participants
     * @param birthYearEnd the ending birth year for eligible participants
     * @param deadline the registration deadline for the event
     */
    public EventDto(int id, String title, String additionalInfo, String address, EventType type, LocalDate date, LocalDate birthYearStart, LocalDate birthYearEnd, LocalDate deadline) {
        this.id = id;
        this.title = title;
        this.additionalInfo = additionalInfo;
        this.address = address;
        this.type = type;
        this.date = date;
        this.birthYearStart = birthYearStart;
        this.birthYearEnd = birthYearEnd;
        this.deadline = deadline;
    }

    /**
     * Default constructor for EventDto.
     */
    public EventDto() {
    }

    /**
     * Constructs an EventDto from an Event object.
     *
     * @param event the Event object to convert to EventDto
     */
    public EventDto(Event event) {
        this.id= event.getId();
        this.title = event.getTitle();
        this.additionalInfo = event.getAdditionalInfo();
        this.address = event.getAddress();
        this.type = event.getType();
        this.date = event.getDate();
        this.birthYearStart = event.getBirthYearStart();
        this.birthYearEnd = event.getBirthYearEnd();
        this.deadline = event.getDeadline();
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
}
