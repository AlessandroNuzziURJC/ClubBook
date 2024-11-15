package clubbook.backend.controller;

import clubbook.backend.model.*;
import clubbook.backend.model.enumClasses.RoleEnum;
import clubbook.backend.model.notification.*;
import clubbook.backend.repository.NotificationRepository;
import clubbook.backend.repository.NotificationTokenRepository;
import clubbook.backend.service.NotificationService;
import clubbook.backend.service.NotificationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class NotificationControllerTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationTokenRepository notificationTokenRepository;

    @InjectMocks
    private NotificationTokenService notificationTokenService;

    @InjectMocks
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.notificationService = new NotificationService(this.notificationRepository);
        this.notificationTokenService = new NotificationTokenService(this.notificationTokenRepository);
        this.notificationController = new NotificationController(this.notificationTokenService, this.notificationService);
    }

    @Test
    void existToken() {
        String token = "TokenExample";
        int id = 1;
        when(this.notificationTokenRepository.existsById(new NotificationTokenId(token, id))).thenReturn(true);
        assertEquals(200, this.notificationController.existToken(id, token).getStatusCode().value());
    }

    @Test
    void existTokenNotExists() {
        String token = "TokenExample";
        int id = 1;
        when(this.notificationTokenRepository.existsById(new NotificationTokenId(token, id))).thenReturn(false);
        assertEquals(404, this.notificationController.existToken(id, token).getStatusCode().value());
    }

    @Test
    void postToken() {
        String token = "TokenExample";
        int id = 1;
        NotificationTokenId notificationTokenId = new NotificationTokenId(token, id);
        NotificationToken notificationToken = new NotificationToken(notificationTokenId);
        when(this.notificationTokenRepository.save(any(NotificationToken.class))).thenReturn(notificationToken);
        assertTrue(this.notificationController.postToken(notificationTokenId).getBody());
    }

    @Test
    void postTokenWrong() {
        String token = "TokenExample";
        int id = 1;
        NotificationTokenId notificationTokenId = new NotificationTokenId(token, id);
        NotificationToken notificationToken = new NotificationToken(notificationTokenId);
        when(this.notificationTokenRepository.save(any(NotificationToken.class))).thenReturn(null);
        assertFalse(this.notificationController.postToken(notificationTokenId).getBody());
    }

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

    @Test
    void getNotifications() {
        int id = 1;
        List<Notification> notificationList = new ArrayList<>(10);

        Role studentRole = new Role();
        studentRole.setRoleId(1);
        studentRole.setName(RoleEnum.STUDENT);
        User user = new User();
        this.createUser(user, 1, "Student1 Name", "Student1 LastName", "student1@gmail.com",
                "student1", "123123123", "C/ Student1",
                LocalDate.of(2001,2,16), true, studentRole);

        for (int i = 0; i < 10; i++) {
            NotificationFactory notificationFactory = new AttendanceNotificationFactory(LocalDate.now(), user);
            notificationFactory.createNotification();
            notificationList.add(notificationFactory.getNotification());
        }

        when(this.notificationService.findByUserId(id)).thenReturn(notificationList);
        assertEquals(notificationList.size(), this.notificationController.getNotifications(id).getBody().size());
    }

}