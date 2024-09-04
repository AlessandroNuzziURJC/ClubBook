package clubbook.backend.controller;

import clubbook.backend.dtos.AttendanceDto;
import clubbook.backend.dtos.ClassGroupAttendanceDto;
import clubbook.backend.dtos.YearsDto;
import clubbook.backend.model.*;
import clubbook.backend.repository.AttendanceRepository;
import clubbook.backend.service.AttendanceService;
import clubbook.backend.service.ClassGroupService;
import clubbook.backend.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.postgresql.hostchooser.HostRequirement.any;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"/scripts/roles_dataset.sql", "/scripts/dataset.sql"})
class AttendanceControllerTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private UserService userService;

    @Mock
    private ClassGroupService classGroupService;

    @InjectMocks
    private AttendanceService attendanceService;

    private AttendanceController attendanceController;

    private List<AttendanceDto> attendanceDtoList;

    private List<Attendance> attendanceList;

    private List<User> studentList;

    private ClassGroup classGroup;

    private void createUser(User user, int id, String firstName, String lastName, String email, String password, String idCard, String address, LocalDate birthday, boolean partner, Role role) {
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setIdCard(idCard);
        user.setAddress(address);
        user.setBirthday(birthday);
        user.setPartner(partner);
        user.setRole(role);
    }

    @BeforeEach
    void setUp() {
        Role studentRole = new Role();
        studentRole.setRoleId(1);
        studentRole.setName(RoleEnum.STUDENT);

        Role teacherRole = new Role();
        teacherRole.setRoleId(2);
        teacherRole.setName(RoleEnum.TEACHER);

        User student1 = new User();
        this.createUser(student1, 1, "Student1 Name", "Student1 LastName", "student1@gmail.com",
                "student1", "123123123", "C/ Student1",
                LocalDate.of(2001,2,16), true, studentRole);

        User student2 = new User();
        this.createUser(student2, 2, "Student2 Name", "Student2 LastName", "student2@gmail.com",
                "student2", "123123123", "C/ Student2",
                LocalDate.of(2001,2,16), true, studentRole);

        User student3 = new User();
        this.createUser(student3, 3, "Student3 Name", "Student3 LastName", "student3@gmail.com",
                "student3", "123123123", "C/ Student3",
                LocalDate.of(2001,2,16), true, studentRole);

        User student4 = new User();
        this.createUser(student4, 4, "Student4 Name", "Student4 LastName", "student4@gmail.com",
                "student4", "123123123", "C/ Student4",
                LocalDate.of(2001,2,16), true, studentRole);
        studentList = new ArrayList<>(Arrays.asList(student1, student2, student3, student4));

        User teacher = new User();
        this.createUser(teacher, 5, "Teacher Name", "Teacher LastName", "teacher@gmail.com",
                "teacher", "123123123", "C/ Teacher",
                LocalDate.of(2001,2,16), true, teacherRole);

        classGroup = new ClassGroup();
        classGroup.setId(1);
        classGroup.setName("Test class");
        classGroup.setStudents(studentList);
        classGroup.setTeachers(new ArrayList<>(Arrays.asList(teacher)));

        List<Integer> attended = new ArrayList<>(Arrays.asList(1, 2));
        List<Integer> notAttended = new ArrayList<>(Arrays.asList(3, 4));
        attendanceDtoList = new ArrayList<>(20);
        for (int i = 0; i < 20; i++) {
            attendanceDtoList.add(new AttendanceDto(LocalDate.of(2024, 1, ++i), 1, attended, notAttended));
        }

        this.attendanceController = new AttendanceController(attendanceService);


        int i = 0;
        this.attendanceList = new ArrayList<>(Arrays.asList(new Attendance(++i, LocalDate.of(2024, 1, 1), studentList.get(i - 1), true),
                new Attendance(++i, LocalDate.of(2024, 1, 1), studentList.get(i - 1), true),
                new Attendance(++i, LocalDate.of(2024, 1, 1), studentList.get(i - 1), false),
                new Attendance(++i, LocalDate.of(2024, 1, 1), studentList.get(i- 1), false)));
    }

    @Test
    void saveAttendances() {
        when(classGroupService.findById(any(Integer.class))).thenReturn(this.classGroup);
        for (int i = 0; i < 4; i++) {
            when(userService.findById(i + 1)).thenReturn(studentList.get(i));
        }
        when(attendanceRepository.saveAll(any(List.class))).thenReturn(attendanceList);
        ResponseEntity<AttendanceDto> attendanceDtoResponseEntity = this.attendanceController.saveAttendances(attendanceDtoList.get(0));
        assertEquals(HttpStatus.OK, attendanceDtoResponseEntity.getStatusCode());
    }

    @Test
    void getAttendances() {
        when(classGroupService.findById(any(Integer.class))).thenReturn(this.classGroup);
        for (int i = 0; i < 4; i++){
            when(attendanceRepository.findAttendance(eq(i + 1), any(LocalDate.class))).thenReturn(this.attendanceList.get(i));
        }
        ResponseEntity<ClassGroupAttendanceDto> attendances = this.attendanceController.getAttendances("2024", "1", "1");
        assertEquals(HttpStatus.OK, attendances.getStatusCode());
    }

    @Test
    void getYears() {
        when(this.attendanceRepository.getYearsUsingClassGroup(1)).thenReturn(new ArrayList<>(List.of("2024")));
        ResponseEntity<List<YearsDto>> years = this.attendanceController.getYears("1");
        assertEquals(HttpStatus.OK, years.getStatusCode());
    }
}