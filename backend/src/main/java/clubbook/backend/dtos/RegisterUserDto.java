package clubbook.backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

/**
 * Data Transfer Object for registering a user.
 * It includes user details such as email, password, personal information, and other attributes.
 */
public class RegisterUserDto {

    /**
     * The email of the user. This field is mandatory and must be a valid email format.
     */
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    /**
     * The password for the user's account. This field is mandatory.
     */
    @NotBlank(message = "Password is mandatory")
    private String password;

    /**
     * The role of the user (e.g., admin, teacher, student). This field is mandatory.
     */
    @NotBlank(message = "Role is mandatory")
    private String role;

    /**
     * The first name of the user. This field is mandatory.
     */
    @NotBlank(message = "First name is mandatory")
    private String firstName;

    /**
     * The last name of the user. This field is mandatory.
     */
    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    /**
     * The phone number of the user. This field is mandatory and must be 9 digits.
     */
    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^[0-9]{9}$", message = "Phone number should be 9 digits")
    private String phoneNumber;

    /**
     * The birthday of the user. This field is mandatory.
     */
    @NotNull(message = "Birthday is mandatory")
    private LocalDate birthday;

    /**
     * The address of the user. This field is mandatory.
     */
    @NotNull(message = "Address is mandatory")
    private String address;

    /**
     * The identification card of the user. This field is optional.
     */
    private String idCard;

    /**
     * Indicates whether the user is a partner. This field is optional.
     */
    private boolean partner;

    /**
     * The profile picture of the user as a byte array. This field is optional.
     */
    private byte[] profilePicture;

    /**
     * Constructs a RegisterUserDto with the specified user details.
     *
     * @param email          the email of the user
     * @param password       the password of the user
     * @param role          the role of the user
     * @param firstName      the first name of the user
     * @param lastName       the last name of the user
     * @param phoneNumber    the phone number of the user
     * @param birthday       the birthday of the user
     * @param address        the address of the user
     * @param idCard         the identification card of the user
     * @param partner        whether the user is a partner
     * @param profilePicture  the profile picture of the user
     */
    public RegisterUserDto(String email, String password, String role, String firstName, String lastName, String phoneNumber, LocalDate birthday, String address, String idCard, boolean partner, byte[] profilePicture) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.address = address;
        this.idCard = idCard;
        this.partner = partner;
        this.profilePicture = profilePicture;
    }

    /**
     * Default constructor for creating an empty RegisterUserDto.
     */
    public RegisterUserDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public void setPartner(boolean partner) {
        this.partner = partner;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }
}
