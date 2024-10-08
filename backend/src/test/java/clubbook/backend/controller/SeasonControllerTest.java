package clubbook.backend.controller;

import clubbook.backend.model.Role;
import clubbook.backend.model.RoleEnum;
import clubbook.backend.model.Season;
import clubbook.backend.model.User;
import clubbook.backend.repository.*;
import clubbook.backend.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
class SeasonControllerTest {

    @Mock
    private SeasonRepository seasonRepository;

    @Mock
    private AttendanceService attendanceService; //Done

    @Mock
    private UserService userService;

    @Mock
    private EventService eventService;

    @InjectMocks
    private SeasonService seasonService; //Done

    private SeasonController seasonController; //Done

    private Season outputSeasonActive;

    private User administrator;

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
        Role adminsitratorRole = new Role();
        adminsitratorRole.setRoleId(1);
        adminsitratorRole.setName(RoleEnum.ADMINISTRATOR);

        this.administrator = new User();
        this.createUser(this.administrator, 5, "Administrator Name", "Administrator LastName", "administrator@gmail.com",
                "administrator", "123123123", "C/ Administrator",
                LocalDate.of(2001,2,16), true, adminsitratorRole);

        this.outputSeasonActive = new Season();
        this.outputSeasonActive.setId(1);
        this.outputSeasonActive.setActive(true);
        this.outputSeasonActive.setInit(LocalDate.of(2024, 9, 1));
        this.outputSeasonActive.setAdminCreator(administrator);

        this.seasonController = new SeasonController(seasonService, attendanceService, eventService);
    }

    @Test
    void isStartedTrueTest() {
        when(seasonRepository.findByActive(true)).thenReturn(outputSeasonActive);
        ResponseEntity<Season> response = this.seasonController.hasStarted();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(outputSeasonActive, response.getBody());
    }

    @Test
    void isStartedFalseTest() {
        when(seasonRepository.findByActive(false)).thenReturn(null);
        ResponseEntity<Season> response = this.seasonController.hasStarted();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void startSeasonTest() {
        when(userService.findById(5)).thenReturn(administrator);
        when(seasonRepository.save(any(Season.class))).thenReturn(null);
        ResponseEntity<Boolean> response = this.seasonController.startSeason(5);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void finishSeasonTest() {
        when(this.seasonRepository.findByActive(true)).thenReturn(outputSeasonActive);
        when(userService.findById(5)).thenReturn(administrator);
        when(seasonRepository.save(any(Season.class))).thenReturn(null);
        doNothing().when(eventService).deleteAll();
        ResponseEntity<Boolean> response = this.seasonController.finishSeason(5);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }
}