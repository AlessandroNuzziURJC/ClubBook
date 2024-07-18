class ClassGroup {
    constructor(nameOrJson, address, idTeachers, schedulesDto) {
        if (typeof nameOrJson === 'object') {
            this.name = nameOrJson.name;
            this.address = nameOrJson.address;
            this.idTeachers = nameOrJson.idTeachers;
            this.schedulesDto = nameOrJson.schedulesDto;
        } else {
            this.name = nameOrJson;
            this.address = address;
            this.idTeachers = idTeachers;
            this.schedulesDto = schedulesDto;
        }
    }

    getName() {
        return this.name;
    }

    setName(_name) {
        this.name = _name;
    }

    getAddress() {
        return this.address;
    }

    setAddress(_address) {
        this.address = _address;
    }

    getIdTeachers() {
        return this.idTeachers;
    }

    setIdTeachers(_idTeachers) {
        this.idTeachers = _idTeachers;
    }

    getSchedulesDto() {
        return this.schedulesDto;
    }

    setSchedulesDto(_schedulesDto) {
        this.schedulesDto = _schedulesDto;
    }
}

export default ClassGroup;
