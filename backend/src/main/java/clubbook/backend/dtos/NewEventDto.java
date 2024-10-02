package clubbook.backend.dtos;

import java.time.LocalDate;

public class NewEventDto {

    private String title;

    private String address;

    private int type;

    private LocalDate date;

    private String additionalInfo;

    private LocalDate birthYearStart;

    private LocalDate birthYearEnd;

    public NewEventDto(String title, String address, int type, LocalDate date, String additionalInfo, LocalDate birthYearStart, LocalDate birthYearEnd) {
        this.title = title;
        this.address = address;
        this.type = type;
        this.date = date;
        this.additionalInfo = additionalInfo;
        this.birthYearStart = birthYearStart;
        this.birthYearEnd = birthYearEnd;
    }

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
}
