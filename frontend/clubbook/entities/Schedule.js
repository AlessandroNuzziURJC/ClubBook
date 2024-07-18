class Schedule {
    constructor(json) {
        if (typeof json === 'object') {
            this.weekDay = this.translate(json.day);
            this.init = json.startTime;
            this.finish = json.endTime;
        } 
    }

    translate(weekDayName) {
        switch (weekDayName) {
            case 'Lunes': return 'MONDAY';
            case 'Martes': return 'TUESDAY';
            case 'Miércoles': return 'WEDNESDAY';
            case 'Jueves': return 'THURSDAY';
            case 'Viernes': return 'FRIDAY';
            case 'Sábado': return 'SATURDAY';
            case 'Domingo': return 'SUNDAY';
        }
    }
};

export default Schedule;