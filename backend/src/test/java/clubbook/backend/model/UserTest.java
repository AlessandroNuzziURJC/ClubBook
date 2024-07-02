package clubbook.backend.model;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    public void testConstructorAndGetters() {
        Role role = new Role(RoleEnum.STUDENT);
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
        User user = new User("John", "Doe", "john.doe@example.com", "password", "1234567890", dateOfBirth, role);

        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals("1234567890", user.getPhoneNumber());
        assertEquals(dateOfBirth, user.getDateOfBirth());
        assertEquals(role, user.getRole());
    }

    @Test
    public void testSetters() {
        Role role1 = new Role(RoleEnum.ADMINISTRATOR);
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
        User user = new User();

        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setEmail("jane.smith@example.com");
        user.setPassword("newpassword");
        user.setPhoneNumber("9876543210");
        user.setDateOfBirth(dateOfBirth);
        user.setRole(role1);

        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("jane.smith@example.com", user.getEmail());
        assertEquals("newpassword", user.getPassword());
        assertEquals("9876543210", user.getPhoneNumber());
        assertEquals(dateOfBirth, user.getDateOfBirth());
        assertEquals(role1, user.getRole());
    }

    @Test
    public void testGetAuthoritiesStudent() {
        Role role = new Role(RoleEnum.STUDENT);
        User user = new User("John", "Doe", "john.doe@example.com", "password", "1234567890", LocalDate.of(1990, 1, 1), role);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_STUDENT")));
    }

    @Test
    public void testGetAuthoritiesTeacher() {
        Role role = new Role(RoleEnum.TEACHER);
        User user = new User("John", "Doe", "john.doe@example.com", "password", "1234567890", LocalDate.of(1990, 1, 1), role);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_TEACHER")));
    }

    @Test
    public void testGetAuthoritiesAdministrator() {
        Role role = new Role(RoleEnum.ADMINISTRATOR);
        User user = new User("John", "Doe", "john.doe@example.com", "password", "1234567890", LocalDate.of(1990, 1, 1), role);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR")));
    }
}