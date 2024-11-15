/**
 * AttendanceDto represents the attendance record for a specific class session.
 */
class AttendanceDto {

    /**
    * Creates an instance of AttendanceDto.
    *
    * @param {Date} date - The date of the class session.
    * @param {string} classGroup - The class group for which attendance is recorded.
    * @param {Array<string>} usersIdsAttended - Array of user IDs of students who attended the class.
    * @param {Array<string>} usersIdsNotAttended - Array of user IDs of students who did not attend the class.
    */
    constructor(date, classGroup, usersIdsAttended, usersIdsNotAttended) {
        this.date = date;
        this.classGroup = classGroup;
        this.usersIdsAttended = usersIdsAttended;
        this.usersIdsNotAttended = usersIdsNotAttended;
    }

    /**
     * Validates the attendance record against the class group.
     *
     * @param {Object} classGroup - The class group object containing student information.
     * @returns {boolean} True if the attendance record is valid, otherwise false.
     */
    validate(classGroup) {
        if (this.date === null || this.classGroup === null || this.usersIdsAttended === null || this.usersIdsNotAttended == null) {
            return false;
        }
        if (this.usersIdsAttended.length + this.usersIdsNotAttended.length !== classGroup.students.length) {
            return false;
        }
        return true;
    }
}

export default AttendanceDto;