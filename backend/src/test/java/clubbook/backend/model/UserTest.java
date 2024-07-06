package clubbook.backend.model;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    public void testConstructorAndGetters() throws Exception {
        Role role = new Role(RoleEnum.STUDENT);
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
        ClassPathResource imgFile = new ClassPathResource("assets/profilepics/profile_blue.png");
        byte[] profilePicture = Files.readAllBytes(imgFile.getFile().toPath());
        User user = new User("John", "Doe", "john.doe@example.com", "password", "1234567890", dateOfBirth, role, "Calle", "123123123", true, profilePicture);

        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals("1234567890", user.getPhoneNumber());
        assertEquals(dateOfBirth, user.getBirthday());
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
        user.setBirthday(dateOfBirth);
        user.setRole(role1);

        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("jane.smith@example.com", user.getEmail());
        assertEquals("newpassword", user.getPassword());
        assertEquals("9876543210", user.getPhoneNumber());
        assertEquals(dateOfBirth, user.getBirthday());
        assertEquals(role1, user.getRole());
    }

    @Test
    public void testGetAuthoritiesStudent() throws Exception {
        Role role = new Role(RoleEnum.STUDENT);
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
        ClassPathResource imgFile = new ClassPathResource("assets/profilepics/profile_blue.png");
        byte[] profilePicture = Files.readAllBytes(imgFile.getFile().toPath());
        User user = new User("John", "Doe", "john.doe@example.com", "password", "1234567890", dateOfBirth, role, "Calle", "123123123", true, profilePicture);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_STUDENT")));
    }

    @Test
    public void testGetAuthoritiesTeacher() throws Exception {
        Role role = new Role(RoleEnum.TEACHER);
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
        ClassPathResource imgFile = new ClassPathResource("assets/profilepics/profile_blue.png");
        byte[] profilePicture = Files.readAllBytes(imgFile.getFile().toPath());
        User user = new User("John", "Doe", "john.doe@example.com", "password", "1234567890", dateOfBirth, role, "Calle", "123123123", true, profilePicture);
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_TEACHER")));
    }

    @Test
    public void testGetAuthoritiesAdministrator() throws Exception {
        Role role = new Role(RoleEnum.ADMINISTRATOR);
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
        ClassPathResource imgFile = new ClassPathResource("assets/profilepics/profile_blue.png");
        byte[] profilePicture = Files.readAllBytes(imgFile.getFile().toPath());
        User user = new User("John", "Doe", "john.doe@example.com", "password", "1234567890", dateOfBirth, role, "Calle", "123123123", true, profilePicture);
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR")));
    }
}