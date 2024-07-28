class ClassGroup {
    constructor(name, id, address, teachers, schedules, students) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.teachers = teachers;
        this.schedules = schedules;
        this.students = students;
    }

    static parseFromJSON(json) {
        return new ClassGroup(json.name, json.id, json.address, json.teachers, json.schedules, json.students);
    }

    validate() {
        let ok = true;
        ok = ok && this.name !== '' && this.address !== '' && this.teachers.length !== 0 && this.schedules.length !== 0;
        ok = ok && this.schedules.every(schedule => {
            return schedule.weekDay !== '' && schedule.init !== '' && schedule.duration !== '';
            
        });
        return ok;
    }
}

export default ClassGroup;
