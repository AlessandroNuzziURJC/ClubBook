class RegisterUserDto {
    constructor(firstName, lastName, email, phoneNumber, birthday, role, address, idCard, partner) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.role = role;
        this.address = address;
        this.idCard = idCard;
        this.partner = partner;
        this.password = idCard;
    }
}

export default RegisterUserDto;