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
}

export default ClassGroup;
