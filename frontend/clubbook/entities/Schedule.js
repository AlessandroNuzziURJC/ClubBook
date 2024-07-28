class Schedule {
    constructor(json) {
        if (typeof json === 'object') {
            this.id = json.id;
            this.weekDay = Schedule.translate(json.weekDay);
            this.init = json.init;
            this.duration = Number(json.duration);
        } 
    }

    calculateEndTime() {
        const [hours, minutes] = this.init.split(':').map(Number);
        const totalMinutes = hours * 60 + minutes + Number(this.duration);
        const endHours = Math.floor(totalMinutes / 60) % 24;
        const endMinutes = totalMinutes % 60;

        return `${endHours.toString().padStart(2, '0')}:${endMinutes.toString().padStart(2, '0')}`;
    }

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