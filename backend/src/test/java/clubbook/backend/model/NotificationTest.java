package clubbook.backend.model;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    @Test
    void testAttendanceNotificationFactory() throws IOException {
        Role role = new Role(RoleEnum.STUDENT);
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
        ClassPathResource imgFile = new ClassPathResource("assets/profilepics/profile_blue.png");
        byte[] profilePicture = Files.readAllBytes(imgFile.getFile().toPath());
        User user = new User("Jane", "Smith", "jane.smith@example.com", "newpassword", "9876543210", dateOfBirth, role, "Calle Olvido, 1", "123123123X", true, profilePicture);
        LocalDate today = LocalDate.of(2024, 4, 21);
        NotificationFactory factory = new AttendanceNotificationFactory(today, user);
        factory.createNotification();
        Notification notification = factory.getNotification();
        assertNotNull(notification);
        assertEquals("Falta de asistencia", notification.getTitle());
        assertEquals(user, notification.getUser());
        assertEquals(today, notification.getDate());
        assertEquals("El alumno Jane Smith no ha asistido a la clase el día 21 de abril 2024", notification.getContent());
    }

}