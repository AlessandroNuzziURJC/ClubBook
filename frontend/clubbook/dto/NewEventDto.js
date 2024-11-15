/**
 * NewEventDto represents the data transfer object for a new event.
 */
class NewEventDto {
    /**
     * Creates an instance of NewEventDto.
     *
     * @param {string} title - The title of the event.
     * @param {string} address - The address where the event will take place.
     * @param {string} type - The type of the event (e.g., workshop, seminar, etc.).
     * @param {Date} date - The date and time when the event will occur.
     * @param {string} additionalInfo - Any additional information about the event.
     * @param {number} birthYearStart - The starting birth year for participants.
     * @param {number} birthYearEnd - The ending birth year for participants.
     */
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