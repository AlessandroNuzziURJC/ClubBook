/**
 * Represents a schedule for a specific class or event.
 */
class Schedule {

    /**
     * Creates an instance of Schedule from a JSON object.
     *
     * @param {Object} json - The JSON object containing schedule information.
     * @param {string} json.id - The unique identifier for the schedule.
     * @param {string} json.weekDay - The day of the week (in Spanish).
     * @param {string} json.init - The start time of the schedule in HH:MM format.
     * @param {number} json.duration - The duration of the schedule in minutes.
     */
    constructor(json) {
        if (typeof json === 'object') {
            this.id = json.id;
            this.weekDay = Schedule.translate(json.weekDay);
            this.init = json.init;
            this.duration = Number(json.duration);
        } 
    }

    /**
     * Calculates the end time of the schedule based on the start time and duration.
     *
     * @returns {string} The end time in HH:MM format.
     */
    calculateEndTime() {
        const [hours, minutes] = this.init.split(':').map(Number);
        const totalMinutes = hours * 60 + minutes + Number(this.duration);
        const endHours = Math.floor(totalMinutes / 60) % 24;
        const endMinutes = totalMinutes % 60;

        return `${endHours.toString().padStart(2, '0')}:${endMinutes.toString().padStart(2, '0')}`;
    }

    /**
     * Translates the given weekday name from Spanish to English.
     *
     * @param {string} weekDayName - The name of the weekday in Spanish.
     * @returns {string} The translated weekday name in English.
     */
    static translate(weekDayName) {
        switch (weekDayName) {
            case 'Lunes': return 'MONDAY';
            case 'Martes': return 'TUESDAY';
            case 'Miércoles': return 'WEDNESDAY';
            case 'Jueves': return 'THURSDAY';
            case 'Viernes': return 'FRIDAY';
            case 'Sábado': return 'SATURDAY';
            case 'Domingo': return 'SUNDAY';
            default: return weekDayName;
        }

    }

    /**
     * Reverses the translation of the given weekday name from English to Spanish.
     *
     * @param {string} weekDayName - The name of the weekday in English.
     * @returns {string} The translated weekday name in Spanish.
     */
    static reverseTranslate(weekDayName) {
        switch (weekDayName) {
            case 'MONDAY': return 'Lunes';
            case 'TUESDAY': return 'Martes';
            case 'WEDNESDAY': return 'Miércoles';
            case 'THURSDAY': return 'Jueves';
            case 'FRIDAY': return 'Viernes';
            case 'SATURDAY': return 'Sábado';
            case 'SUNDAY': return 'Domingo';
            default: return weekDayName;
        }
    }
};

export default Schedule;