package clubbook.backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Data Transfer Object for updating user information.
 * This class is used to carry the data required to update a user's details.
 */
public class UpdateUserDto {

    /**
     * The first name of the user. This field is mandatory and cannot be blank.
     */
    @NotBlank(message = "First name is mandatory")
    private String firstName;

    /**
     * The last name of the user. This field is mandatory and cannot be blank.
     */
    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    /**
     * The email of the user. This field is mandatory, cannot be blank,
     * and must be a valid email format.
     */
    @Email
    @NotBlank(message = "Email is mandatory")
    private String email;

    /**
     * The phone number of the user. This field is mandatory and cannot be blank.
     */
    @NotBlank(message = "Phone number is mandatory")
    private String phoneNumber;

    /**
     * The birthday of the user. This field is mandatory and cannot be null.
     */
    @NotNull(message = "Birthday is mandatory")
    private LocalDate birthday;

    /**
     * The address of the user. This field is mandatory and cannot be blank.
     */
    @NotBlank(message = "Address is mandatory")
    private String address;

    /**
     * The ID card of the user. This field is mandatory and cannot be blank.
     */
    @NotBlank(message = "IdCard is mandatory")
    private String idCard;

    /**
     * Indicates whether the user is a partner. This field is mandatory.
     */
    @NotNull(message = "Partner is mandatory")
    private boolean partner;

    /**
     * Default constructor for creating an empty UpdateUserDto.
     */
    public UpdateUserDto() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public boolean isPartner() {
        return partner;
    }

    public void setPartner(@NotBlank(message = "Partner is mandatory") boolean partner) {
        this.partner = partner;
    }
}
