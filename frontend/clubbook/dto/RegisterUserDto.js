/**
 * RegisterUserDto represents the data transfer object for registering a new user.
 */
class RegisterUserDto {

    /**
     * Creates an instance of RegisterUserDto.
     *
     * @param {string} firstName - The first name of the user.
     * @param {string} lastName - The last name of the user.
     * @param {string} email - The email address of the user.
     * @param {string} phoneNumber - The phone number of the user.
     * @param {Date} birthday - The birth date of the user.
     * @param {string} role - The role assigned to the user (e.g., admin, user).
     * @param {string} address - The address of the user.
     * @param {string} idCard - The ID card number of the user.
     * @param {string} partner - The partner information for the user.
     */
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