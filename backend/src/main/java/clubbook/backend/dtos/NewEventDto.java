package clubbook.backend.dtos;

import java.time.LocalDate;

/**
 * Data Transfer Object for creating a new event.
 * Contains information such as title, address, event type, date, additional info, age range, and registration deadline.
 */
public class NewEventDto {

    /**
     * The title of the event.
     */
    private String title;

    /**
     * The address where the event will take place.
     */
    private String address;

    /**
     * The type of event, represented as an integer.
     */
    private int type;

    /**
     * The date of the event.
     */
    private LocalDate date;

    /**
     * Additional information about the event.
     */
    private String additionalInfo;

    /**
     * The earliest birth year allowed for participants.
     */
    private LocalDate birthYearStart;

    /**
     * The latest birth year allowed for participants.
     */
    private LocalDate birthYearEnd;

    /**
     * The registration deadline for the event.
     */
    private LocalDate deadline;

    /**
     * Constructs a NewEventDto with specified values.
     *
     * @param title the title of the event
     * @param address the address of the event
     * @param type the type of the event
     * @param date the date of the event
     * @param additionalInfo additional information about the event
     * @param birthYearStart the earliest birth year for participants
     * @param birthYearEnd the latest birth year for participants
     * @param deadline the registration deadline
     */
    public NewEventDto(String title, String address, int type, LocalDate date, String additionalInfo, LocalDate birthYearStart, LocalDate birthYearEnd, LocalDate deadline) {
        this.title = title;
        this.address = address;
        this.type = type;
        this.date = date;
        this.additionalInfo = additionalInfo;
        this.birthYearStart = birthYearStart;
        this.birthYearEnd = birthYearEnd;
        this.deadline = deadline;
    }

    /**
     * Default constructor.
     */
    public NewEventDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
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
