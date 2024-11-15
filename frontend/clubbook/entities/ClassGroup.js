/**
 * ClassGroup represents a group of students and their associated details.
 */
class ClassGroup {

    /**
     * Creates an instance of ClassGroup.
     *
     * @param {string} name - The name of the class group.
     * @param {string} id - The unique identifier for the class group.
     * @param {string} address - The address where the class group is located.
     * @param {Array<string>} teachers - An array of teachers assigned to the class group.
     * @param {Array<Object>} schedules - An array of schedule objects containing class timings.
     * @param {Array<string>} students - An array of student identifiers in the class group.
     */
    constructor(name, id, address, teachers, schedules, students) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.teachers = teachers;
        this.schedules = schedules;
        this.students = students;
    }

    /**
     * Parses a ClassGroup object from a JSON object.
     *
     * @param {Object} json - The JSON object to parse.
     * @returns {ClassGroup} The ClassGroup instance created from the JSON object.
     */
    static parseFromJSON(json) {
        return new ClassGroup(json.name, json.id, json.address, json.teachers, json.schedules, json.students);
    }

    /**
     * Validates the class group data.
     *
     * @returns {boolean} True if the class group is valid; otherwise, false.
     */
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
