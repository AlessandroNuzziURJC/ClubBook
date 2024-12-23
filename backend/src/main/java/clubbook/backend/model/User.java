package clubbook.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "T_User")
public class User implements UserDetails {

    /**
     * The unique identifier for user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The first name of the user. Can't be null.
     */
    @Column(nullable = false)
    private String firstName;

    /**
     * The last name of the user. Can't be null.
     */
    @Column(nullable = false)
    private String lastName;

    /**
     * The email of the user. Can't be null. It's unique.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * The password of the user. Can't be null.
     */
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    /**
     * The phone number of the user. Can't be null.
     */
    @Column(nullable = false, length = 20)
    private String phoneNumber;

    /**
     * The birthday of the user. Can't be null.
     */
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate birthday;

    /**
     * The address of the user. Can't be null.
     */
    @Column(nullable = false)
    private String address;

    /**
     * The id card of the user. Can't be null.
     */
    @Column(nullable = false)
    private String idCard;

    /**
     * The role of the user. Can't be null.
     */
    @ManyToOne
    @JoinColumn(name = "role_fk_id", referencedColumnName = "roleId", nullable = false)
    private Role role;

    /**
     * User is partner or not. Can't be null.
     */
    @Column(nullable = false)
    private boolean partner;

    /**
     * User has access to the system. Can't be null.
     */
    @Column(nullable = false)
    private boolean allowedAccess = true;

    /**
     * User's profile picture. Can't be null.
     */
    @JsonIgnore
    @Column(name = "profile_picture", columnDefinition="BYTEA", nullable = false)
    private byte[] profilePicture;

    /**
     * Constructs a User with specified parameters.
     *
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param email the email address of the user
     * @param password the password for the user account
     * @param phoneNumber the phone number of the user
     * @param birthday the birth date of the user
     * @param role the role assigned to the user
     * @param address the address of the user
     * @param idCard the identification card number of the user
     * @param partner whether the user is a partner
     * @param profilePicture the profile picture of the user as a byte array
     */
    public User(String firstName, String lastName, String email, String password, String phoneNumber, LocalDate birthday, Role role, String address, String idCard, boolean partner, byte[] profilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.role = role;
        this.address = address;
        this.idCard = idCard;
        this.partner = partner;
        this.profilePicture = profilePicture;
    }

    /**
     * Default constructor for User.
     */
    public User() {}

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public Role getRole() {
        return role;
    }

    public String getAddress() {
        return address;
    }

    public String getIdCard() {
        return idCard;
    }

    public boolean isPartner() {
        return partner;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public boolean isAllowedAccess() {
        return allowedAccess;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setPartner(boolean partner) {
        this.partner = partner;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setAllowedAccess(boolean allowedAccess) {
        this.allowedAccess = allowedAccess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && partner == user.partner && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(birthday, user.birthday) && Objects.equals(address, user.address) && Objects.equals(idCard, user.idCard) && Objects.equals(role, user.role) && Objects.deepEquals(profilePicture, user.profilePicture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password, phoneNumber, birthday, address, idCard, role, partner, Arrays.hashCode(profilePicture));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getName().toString());

        return List.of(authority);
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}