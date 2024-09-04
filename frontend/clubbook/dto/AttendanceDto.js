class AttendanceDto {

    constructor(date, classGroup, usersIdsAttended, usersIdsNotAttended) {
        this.date = date;
        this.classGroup = classGroup;
        this.usersIdsAttended = usersIdsAttended;
        this.usersIdsNotAttended = usersIdsNotAttended;
    }

    validate(classGroup) {
        if (this.date === null || this.classGroup === null || this. usersIdsAttended === null || this.usersIdsNotAttended == null) {
            return false;
        }
        if (this.usersIdsAttended.length + this.usersIdsNotAttended.length !== classGroup.students.length){
            return false;
        }
        return true;
    }
}

export default AttendanceDto;