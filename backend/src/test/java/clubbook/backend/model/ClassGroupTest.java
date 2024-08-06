package clubbook.backend.model;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClassGroupTest {

    @Test
    void settersAndGetters() throws IOException {
        ClassGroup classGroup = new ClassGroup();
        classGroup.setName("Grupo 1");
        classGroup.setAddress("Ejemplo");

        List<User> students = new ArrayList<>(3);
        Role role = new Role(RoleEnum.STUDENT);
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
        ClassPathResource imgFile = new ClassPathResource("assets/profilepics/profile_blue.png");
        byte[] profilePicture = Files.readAllBytes(imgFile.getFile().toPath());
        students.add(new User("Jane", "Smith", "jane.smith@example.com", "newpassword", "9876543210", dateOfBirth, role, "Calle Olvido, 1", "123123123X", true, profilePicture));
        students.add(new User("Jane", "Smith2", "jane.smith2@example.com", "newpassword", "9876543210", dateOfBirth, role, "Calle Olvido, 11", "123123124X", true, profilePicture));
        students.add(new User("Jane", "Smith3", "jane.smith3@example.com", "newpassword", "9876543210", dateOfBirth, role, "Calle Olvido, 12", "123123125X", false, profilePicture));
        classGroup.setStudents(students);

        List<User> teachers = new ArrayList<>(3);
        Role roleTeacher = new Role(RoleEnum.TEACHER);
        dateOfBirth = LocalDate.of(1980, 1, 1);
        imgFile = new ClassPathResource("assets/profilepics/profile_blue.png");
        profilePicture = Files.readAllBytes(imgFile.getFile().toPath());
        teachers.add(new User("Jane", "Smith", "jane.smith@example.com", "newpassword", "9876543210", dateOfBirth, roleTeacher, "Calle Olvido, 1", "123123123X", true, profilePicture));
        teachers.add(new User("Jane", "Smith2", "jane.smith2@example.com", "newpassword", "9876543210", dateOfBirth, roleTeacher, "Calle Olvido, 11", "123123124X", true, profilePicture));
        teachers.add(new User("Jane", "Smith3", "jane.smith3@example.com", "newpassword", "9876543210", dateOfBirth, roleTeacher, "Calle Olvido, 12", "123123125X", false, profilePicture));
        classGroup.setTeachers(teachers);

        List<Schedule> schedules = new ArrayList<>(2);
        schedules.add(new Schedule(1, WeekDayEnum.MONDAY, LocalTime.of(16, 0, 0), 1));
        schedules.add(new Schedule(2, WeekDayEnum.WEDNESDAY, LocalTime.of(16, 0, 0), 1));
        classGroup.setSchedules(schedules);

        assertEquals("Grupo 1", classGroup.getName());
        assertEquals("Ejemplo", classGroup.getAddress());
        assertEquals(students.size(), classGroup.getStudents().size());
        assertEquals(teachers.size(), classGroup.getTeachers().size());
        assertEquals(schedules.size(), classGroup.getSchedules().size());
    }
}