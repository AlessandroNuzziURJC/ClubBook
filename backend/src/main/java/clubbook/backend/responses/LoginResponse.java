package clubbook.backend.responses;

import clubbook.backend.model.User;

/**
 * Represents the response returned upon a successful login.
 * Contains the authentication token, its expiration time, and the logged-in user's information.
 */
public class LoginResponse {

    /**
     * Token for authentication in requests.
     */
    private String token;

    /**
     * Expiration time of the token.
     */
    private long expiresIn;

    /**
     * User logged in associated to token.
     */
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
