package clubbook.backend.controller;

import clubbook.backend.dtos.LoginUserDto;
import clubbook.backend.dtos.RegisterUserDto;
import clubbook.backend.model.Role;
import clubbook.backend.model.RoleEnum;
import clubbook.backend.model.User;
import clubbook.backend.repository.UserRepository;
import clubbook.backend.responses.LoginResponse;
import clubbook.backend.responses.ResponseWrapper;
import clubbook.backend.service.AuthenticationService;
import clubbook.backend.service.JwtService;
import clubbook.backend.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthenticationControllerTest {

    @Mock
    private RoleService roleService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(userRepository, authenticationManager, passwordEncoder, roleService);
        authenticationController = new AuthenticationController(jwtService, authenticationService);
    }

    @Test
    void testRegisterStudent_Success() {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setEmail("student@prueba.com");
        registerUserDto.setPassword("password");
        registerUserDto.setRole(RoleEnum.STUDENT.name());
        registerUserDto.setFirstName("Marty");
        registerUserDto.setLastName("McFly");
        registerUserDto.setPhoneNumber("767676767");
        registerUserDto.setBirthday(LocalDate.of(1968, 6, 9));

        Role role = new Role(RoleEnum.STUDENT);

        when(roleService.findByName(any(RoleEnum.class))).thenReturn(role);

        User user = new User();
        user.setEmail("student@prueba.com");
        user.setPassword("password");
        user.setRole(roleService.findByName(RoleEnum.STUDENT));
        user.setFirstName("Marty");
        user.setLastName("McFly");
        user.setPhoneNumber("767676767");
        user.setBirthday(LocalDate.of(1968, 6, 9));

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        ResponseEntity<ResponseWrapper<User>> response = authenticationController.register(registerUserDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody().getData());
    }

    @Test
    void testRegisterStudent_BadRequest() {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        // Intentionally leaving out the email field
        registerUserDto.setPassword("password");
        registerUserDto.setRole(RoleEnum.STUDENT.name());
        registerUserDto.setFirstName("Marty");
        registerUserDto.setLastName("McFly");
        registerUserDto.setPhoneNumber("767676767");
        registerUserDto.setBirthday(LocalDate.of(1968, 6, 9));

        Role role = new Role(RoleEnum.STUDENT);

        when(roleService.findByName(any(RoleEnum.class))).thenReturn(role);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        ResponseEntity<ResponseWrapper<User>> response = authenticationController.register(registerUserDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void logInStudentCorrect() {
        User user = new User();
        user.setEmail("student@prueba.com");
        user.setPassword("password");
        user.setRole(roleService.findByName(RoleEnum.STUDENT));
        user.setFirstName("Marty");
        user.setLastName("McFly");
        user.setPhoneNumber("767676767");
        user.setBirthday(LocalDate.of(1968, 6, 9));

        LoginUserDto loginUserDto = new LoginUserDto("student@prueba.com", "password");

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        ResponseEntity<ResponseWrapper<LoginResponse>> response = authenticationController.authenticate(loginUserDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.getEmail(), response.getBody().getData().getUser().getEmail());
        assertEquals(user.getPassword(), response.getBody().getData().getUser().getPassword());
    }

    @Test
    void logInStudentBadCredentials() {
        User user = new User();
        user.setEmail("student@prueba.com");
        user.setPassword("password");
        user.setRole(roleService.findByName(RoleEnum.STUDENT));
        user.setFirstName("Marty");
        user.setLastName("McFly");
        user.setPhoneNumber("767676767");
        user.setBirthday(LocalDate.of(1968, 6, 9));

        LoginUserDto loginUserDto = new LoginUserDto("student@prueba.com", "password");

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        try {
            authenticationController.authenticate(loginUserDto);
        } catch (NoSuchElementException e) {
        }

    }
}
