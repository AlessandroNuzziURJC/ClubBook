package clubbook.backend.dtos;

/**
 * Data Transfer Object for user login information.
 * Contains the email and password required for user authentication.
 */
public class LoginUserDto {

    /**
     * User's email address.
     */
    private String email;

    /**
     * User's password.
     */
    private String password;

    /**
     * Constructs a LoginUserDto with specified email and password.
     *
     * @param email the user's email address
     * @param password the user's password
     */
    public LoginUserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
