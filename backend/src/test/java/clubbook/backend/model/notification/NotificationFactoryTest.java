package clubbook.backend.model.notification;

import clubbook.backend.model.Role;
import clubbook.backend.model.User;
import clubbook.backend.model.enumClasses.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class NotificationFactoryTest {
    private NotificationFactory notificationFactory;
    private User user;

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

        user = new User();
        this.createUser(user, 1, "Student1 Name", "Student1 LastName", "student1@gmail.com",
                "student1", "123123123", "C/ Student1",
                LocalDate.of(2001,2,16), true, studentRole);
    }

    @Test
    void createAttendanceNotification() {
        this.notificationFactory = new AttendanceNotificationFactory(LocalDate.now(), this.user);
        this.notificationFactory.createNotification();
        Notification notification = this.notificationFactory.getNotification();
        assertEquals("Falta de asistencia", notification.getTitle());
        assertEquals(LocalDate.now(), notification.getDate());
        assertEquals("El alumno Student1 Name Student1 LastName no ha asistido a la clase el día " +
                notification.getDate().getDayOfMonth() + " de " +
                notificationFactory.getSpanishMonth(notification.getDate().getMonthValue()) + " " +
                notification.getDate().getYear(), notification.getContent());
    }

    @Test
    void createDeleteEventNotification() {
        this.notificationFactory = new DeleteEventNotificationFactory(LocalDate.now(), this.user);
        this.notificationFactory.createNotification();
        Notification notification = this.notificationFactory.getNotification();
        assertEquals("Evento cancelado", notification.getTitle());
        assertEquals(LocalDate.now(), notification.getDate());
        assertEquals("Se ha cancelado el evento del día " + notification.getDate().getDayOfMonth() + " de " +
                notificationFactory.getSpanishMonth(notification.getDate().getMonthValue()) + " " +
                + notification.getDate().getYear() + ". Para más información consulte al profesor.", notification.getContent());
    }

    @Test
    void createEventInscriptionFinishedNotification() {
        this.notificationFactory = new EventInscriptionFinishedNotificationFactory(LocalDate.now(), this.user);
        this.notificationFactory.createNotification();
        Notification notification = this.notificationFactory.getNotification();
        assertEquals("¡Fin de inscripción!", notification.getTitle());
        assertEquals(LocalDate.now(), notification.getDate());
        assertEquals("Ha finalizado la inscripción al evento del día " +
                notification.getDate().getDayOfMonth() + " de "
                + notificationFactory.getSpanishMonth(notification.getDate().getMonthValue()) + " " +
                + notification.getDate().getYear() +". Consulta los asistentes en la pestaña de eventos.", notification.getContent());
    }

    @Test
    void createEventInscriptionLimitNotification() {
        this.notificationFactory = new EventInscriptionLimitNotificationFactory(LocalDate.now(), this.user);
        this.notificationFactory.createNotification();
        Notification notification = this.notificationFactory.getNotification();
        assertEquals("¡Fin de inscripción!", notification.getTitle());
        assertEquals(LocalDate.now(), notification.getDate());
        assertEquals("¡Está a punto de finalizar el plazo de inscripción para el evento del día " +
                + notification.getDate().getDayOfMonth() + " de "
                + notificationFactory.getSpanishMonth(notification.getDate().getMonthValue()) + " " +
                + notification.getDate().getYear() + "! ¡Inscríbete!", notification.getContent());
    }

    @Test
    void createEventReminderNotification() {
        this.notificationFactory = new EventReminderNotificationFactory(LocalDate.now(), this.user);
        this.notificationFactory.createNotification();
        Notification notification = this.notificationFactory.getNotification();
        assertEquals("¡Recuerda!", notification.getTitle());
        assertEquals(LocalDate.now(), notification.getDate());
        assertEquals("¡El próximo día " +
                + notification.getDate().getDayOfMonth() + " de "
                + notificationFactory.getSpanishMonth(notification.getDate().getMonthValue()) + " " +
                + notification.getDate().getYear() + " hay un evento! Para más información consulte la pestaña de eventos.", notification.getContent());
    }

    @Test
    void createModifyEventNotification() {
        this.notificationFactory = new ModifyEventNotificationFactory(LocalDate.now(), this.user);
        this.notificationFactory.createNotification();
        Notification notification = this.notificationFactory.getNotification();
        assertEquals("Evento modificado", notification.getTitle());
        assertEquals(LocalDate.now(), notification.getDate());
        assertEquals("Ha habido una actualización en la información del evento del día " +
                + notification.getDate().getDayOfMonth() + " de "
                + notificationFactory.getSpanishMonth(notification.getDate().getMonthValue()) + " " +
                + notification.getDate().getYear() + ". No olvide consultar la nueva información en la pestaña de eventos.", notification.getContent());
    }

    @Test
    void createNewEventNotification() {
        this.notificationFactory = new NewEventNotificationFactory(LocalDate.now(), this.user);
        this.notificationFactory.createNotification();
        Notification notification = this.notificationFactory.getNotification();
        assertEquals("Nuevo evento", notification.getTitle());
        assertEquals(LocalDate.now(), notification.getDate());
        assertEquals("¡El próximo día " +
                + notification.getDate().getDayOfMonth() + " de "
                + notificationFactory.getSpanishMonth(notification.getDate().getMonthValue()) + " " +
                + notification.getDate().getYear() +
                " hay un nuevo evento! Para más información consulte la pestaña de eventos. " +
                "No olvide inscribirse al evento si desea acudir.", notification.getContent());
    }
}