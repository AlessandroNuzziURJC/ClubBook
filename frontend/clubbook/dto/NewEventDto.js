class NewEventDto {
    constructor(title, address, type, date, additionalInfo, birthYearStart, birthYearEnd) {
        this.title = title;
        this.address = address;
        this.type = type;
        this.date = date;
        this.additionalInfo = additionalInfo;
        this.birthYearStart = birthYearStart;
        this.birthYearEnd = birthYearEnd;
    }
}

export default NewEventDto;