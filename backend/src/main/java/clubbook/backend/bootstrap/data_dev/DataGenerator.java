package clubbook.backend.bootstrap.data_dev;

import clubbook.backend.dtos.RegisterUserDto;
import clubbook.backend.model.*;
import clubbook.backend.model.enumClasses.EventTypeEnum;
import clubbook.backend.model.enumClasses.RoleEnum;
import clubbook.backend.repository.RoleRepository;
import clubbook.backend.service.AuthenticationService;
import clubbook.backend.service.EventService;
import clubbook.backend.service.RoleService;
import clubbook.backend.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Date;

/**
 * Service class for generating initial data for the application.
 * This class populates the database with default roles, event types,
 * and an initial administrator user upon application startup.
 */
@Service
public class DataGenerator {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;
    @Autowired
    private EventService eventService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Method to initialize data in the database.
     * This method is called after the bean's properties have been set.
     * It creates default roles, event types, and an initial administrator user
     * if no roles exist in the database.
     *
     * @throws IOException if there is an issue reading the profile picture file.
     */
    @PostConstruct
    public void init() throws IOException {
        if (roleRepository.count() != 0) {
            return ;
        }

        EventTypeEnum[] eventTypes = EventTypeEnum.values();
        EventType eventType;
        for (EventTypeEnum eventTypeEnum : eventTypes) {
            eventType = new EventType();
            eventType.setName(eventTypeEnum);
            eventType.setCreatedAt(new Date());
            this.eventService.saveEventType(eventType);
        }

        RoleEnum[] rolesEnum = RoleEnum.values();
        Role role;
        for (RoleEnum roleEnumValue : rolesEnum) {
            role = new Role(roleEnumValue);
            this.roleService.save(role);
        }

        Role studentRole = roleService.findByName(RoleEnum.STUDENT);
        Role teacherRole = roleService.findByName(RoleEnum.TEACHER);
        Role administratorRole = roleService.findByName(RoleEnum.ADMINISTRATOR);

        Path imagePath = Paths.get("src/main/resources/assets/profilepics/profile_blue.png");
        byte[] imageBytes = Files.readAllBytes(imagePath);

        authenticationService.signup(new RegisterUserDto("admindefault@clubbook.com", "abcd", "administrator",
                "Administrator Default", "Default default", "000000000",
                LocalDate.of(2000, 1, 1), "Admin", "000000000A", false, imageBytes));
    }
}
