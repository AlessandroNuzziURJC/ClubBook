package clubbook.backend.controller;

import clubbook.backend.dtos.RegisterClassGroupDto;
import clubbook.backend.dtos.ScheduleDto;
import clubbook.backend.model.ClassGroup;
import clubbook.backend.model.Role;
import clubbook.backend.model.Schedule;
import clubbook.backend.model.User;
import clubbook.backend.model.enumClasses.RoleEnum;
import clubbook.backend.model.enumClasses.WeekDayEnum;
import clubbook.backend.service.ClassGroupService;
import clubbook.backend.service.SeasonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ClassGroupControllerTest {

    @Mock
    private ClassGroupService classGroupService;

    @Mock
    private SeasonService seasonService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ClassGroupController classGroupController;

    private List<ClassGroup> classGroups = new ArrayList<>();

    private User s1, s2, s3, s4, s5, s6, t1, t2;

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

        s1 = new User();
        this.createUser(s1, 1, "Student1 Name", "Student1 LastName", "student1@gmail.com",
                "student1", "123123123", "C/ Student1",
                LocalDate.of(2001,2,16), true, studentRole);

        s2 = new User();
        this.createUser(s2, 2, "Student2 Name", "Student2 LastName", "student2@gmail.com",
                "student2", "123123123", "C/ Student2",
                LocalDate.of(2001,2,16), true, studentRole);

        s3 = new User();
        this.createUser(s3, 3, "Student3 Name", "Student3 LastName", "student3@gmail.com",
                "student3", "123123123", "C/ Student3",
                LocalDate.of(2001,2,16), true, studentRole);

        s4 = new User();
        this.createUser(s4, 4, "Student4 Name", "Student4 LastName", "student4@gmail.com",
                "student4", "123123123", "C/ Student4",
                LocalDate.of(2001,2,16), true, studentRole);

        s5 = new User();
        this.createUser(s5, 5, "Student5 Name", "Student5 LastName", "student5@gmail.com",
                "student5", "123123123", "C/ Student5",
                LocalDate.of(2001,2,16), true, studentRole);

        s6 = new User();
        this.createUser(s6, 6, "Student6 Name", "Student6 LastName", "student6@gmail.com",
                "student6", "123123123", "C/ Student6",
                LocalDate.of(2001,2,16), true, studentRole);

        t1 = new User();
        this.createUser(t1, 7, "Teacher1 Name", "Teacher1 LastName", "teacher1@gmail.com",
                "teacher1", "123123123", "C/ Teacher1",
                LocalDate.of(2001,2,16), true, teacherRole);

        t2 = new User();
        this.createUser(t2, 8, "Teacher2 Name", "Teacher2 LastName", "teacher2@gmail.com",
                "teacher2", "123123123", "C/ Teacher2",
                LocalDate.of(2001,2,16), true, teacherRole);

        ClassGroup classGroup = new ClassGroup();
        classGroup.setId(1);
        classGroup.setName("Test Group 1");
        classGroup.setAddress("Test Address 1");
        classGroup.setSchedules(List.of(
                new Schedule(1, WeekDayEnum.MONDAY, LocalTime.of(18, 0, 0), 60),
                new Schedule(2, WeekDayEnum.WEDNESDAY, LocalTime.of(18, 0, 0), 60)));
        classGroup.setTeachers(List.of(t1, t2));
        classGroup.setStudents(List.of(s1, s2, s3));
        classGroups.add(classGroup);
        classGroup = new ClassGroup();
        classGroup.setId(2);
        classGroup.setName("Test Group 2");
        classGroup.setAddress("Test Address 2");
        classGroup.setSchedules(List.of(
                new Schedule(3, WeekDayEnum.TUESDAY, LocalTime.of(18, 0, 0), 60),
                new Schedule(4, WeekDayEnum.THURSDAY, LocalTime.of(18, 0, 0), 60)));
        classGroup.setTeachers(List.of(t1));
        classGroup.setStudents(List.of(s4, s5, s6));
        classGroups.add(classGroup);

    }


    @Test
    void getAllClassGroupsTest(){
        when(seasonService.seasonStarted()).thenReturn(true);
        GrantedAuthority teacherAuthority = mock(GrantedAuthority.class);
        when(teacherAuthority.getAuthority()).thenReturn("ROLE_TEACHER");
        when(authentication.getAuthorities()).thenReturn((Collection) List.of(teacherAuthority));

        when(this.classGroupService.getAllClassGroups()).thenReturn(this.classGroups);
        assertEquals(HttpStatus.OK, this.classGroupController.getAllClassGroups().getStatusCode());
    }

    @Test
    void getClassGroupTest(){
        when(seasonService.seasonStarted()).thenReturn(true);
        GrantedAuthority teacherAuthority = mock(GrantedAuthority.class);
        when(teacherAuthority.getAuthority()).thenReturn("ROLE_ADMINISTRATOR");
        when(authentication.getAuthorities()).thenReturn((Collection) List.of(teacherAuthority));

        when(this.classGroupService.getClassGroup(1)).thenReturn(this.classGroups.get(0));
        assertEquals(HttpStatus.OK, this.classGroupController.getClassGroup(1).getStatusCode());
    }

    @Test
    void createClassGroup(){
        when(seasonService.seasonStarted()).thenReturn(true);

        List<ScheduleDto> schedulesDto = List.of(
                new ScheduleDto(5, WeekDayEnum.TUESDAY.name(), LocalTime.of(18, 0, 0), 60),
                new ScheduleDto(6, WeekDayEnum.THURSDAY.name(), LocalTime.of(18, 0, 0), 60));
        RegisterClassGroupDto registerClassGroupDto = new RegisterClassGroupDto("Test Group 3", "Test Address 3", List.of(t2.getId()), schedulesDto);
        ClassGroup classGroup = new ClassGroup();
        classGroup.setId(3);
        classGroup.setName("Test Group 3");
        classGroup.setAddress("Test Address 3");
        classGroup.setTeachers(List.of(t2));
        classGroup.setSchedules(List.of(
                new Schedule(5, WeekDayEnum.TUESDAY, LocalTime.of(18, 0, 0), 60),
                new Schedule(6, WeekDayEnum.THURSDAY, LocalTime.of(18, 0, 0), 60)));
        when(this.classGroupService.create(registerClassGroupDto)).thenReturn(classGroup);
        assertEquals(HttpStatus.OK, this.classGroupController.createClassGroup(registerClassGroupDto).getStatusCode());
    }

    @Test
    void deleteClassGroupTest() {
        when(seasonService.seasonStarted()).thenReturn(true);

        doNothing().when(classGroupService).delete(1);
        assertEquals(HttpStatus.OK, this.classGroupController.deleteClassGroup(1).getStatusCode());
    }

    @Test
    void modifyClassGroupTest() {
        when(seasonService.seasonStarted()).thenReturn(true);

        ClassGroup classGroup = new ClassGroup();
        classGroup.setId(3);
        classGroup.setName("Test Group 3");
        classGroup.setAddress("Test Address 3");
        classGroup.setTeachers(List.of(t2));
        classGroup.setSchedules(List.of(
                new Schedule(5, WeekDayEnum.TUESDAY, LocalTime.of(18, 0, 0), 60),
                new Schedule(6, WeekDayEnum.THURSDAY, LocalTime.of(18, 0, 0), 60)));
        when(this.classGroupService.findById(3)).thenReturn(classGroup);

        List<ScheduleDto> schedulesDto = List.of(
                new ScheduleDto(5, WeekDayEnum.TUESDAY.name(), LocalTime.of(18, 0, 0), 60),
                new ScheduleDto(6, WeekDayEnum.THURSDAY.name(), LocalTime.of(18, 0, 0), 60));
        RegisterClassGroupDto registerClassGroupDto = new RegisterClassGroupDto("Test Group 3 Mod", "Test Address 3 Mod", List.of(t2.getId()), schedulesDto);
        assertEquals(HttpStatus.OK, this.classGroupController.modifyClassGroup(registerClassGroupDto, 3).getStatusCode());
    }

    @Test
    void addNewStudentsClassGroupTest() {
        when(seasonService.seasonStarted()).thenReturn(true);

        List<User> users = List.of(s4, s5, s6);
        when(classGroupService.addNewStudentsClassGroup(1, List.of(s4.getId(), s5.getId(), s6.getId()))).thenReturn(users);

        assertEquals(HttpStatus.OK, this.classGroupController.addNewStudentsClassGroup(1, List.of(s4.getId(), s5.getId(), s6.getId())).getStatusCode());
    }

    @Test
    void removeStudentsClassGroup() {
        when(seasonService.seasonStarted()).thenReturn(true);

        List<User> users = List.of(s1, s3);
        when(classGroupService.addNewStudentsClassGroup(1, List.of(s1.getId(), s3.getId()))).thenReturn(users);

        assertEquals(HttpStatus.OK, this.classGroupController.addNewStudentsClassGroup(1, List.of(s1.getId(), s3.getId())).getStatusCode());

    }
}