package clubbook.backend.controller;

import clubbook.backend.model.Role;
import clubbook.backend.model.RoleEnum;
import clubbook.backend.model.User;
import clubbook.backend.repository.UserRepository;
import clubbook.backend.service.AuthenticationService;
import clubbook.backend.service.JwtService;
import clubbook.backend.service.RoleService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleService roleService;

/*    @Mock
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
    private AuthenticationController authenticationController;*/

    @Autowired
    private ObjectMapper objectMapper;

    private String extractValue(String response, String key) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get(key).asText();
    }

/*    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(userRepository, authenticationManager, passwordEncoder, roleService);
        authenticationController = new AuthenticationController(jwtService, authenticationService);
    }*/

    @Transactional
    @Test
    public void testCreateUserCorrect() throws Exception {
        Role role = new Role(RoleEnum.STUDENT);
        //when(roleService.findByName(any(RoleEnum.class))).thenReturn(role);
        //when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        User user = new User();
        user.setEmail("student@prueba.com");
        user.setPassword("password");
        user.setRole(roleService.findByName(RoleEnum.STUDENT));
        user.setFirstName("Marty");
        user.setLastName("McFly");
        user.setPhoneNumber("767676767");
        user.setBirthday(LocalDate.of(1968, 6, 9));
        String userJson = "{"
                + "\"email\": \"student@prueba.com\","
                + "\"password\": \"password\","
                + "\"role\": \"STUDENT\","
                + "\"firstName\": \"Marty\","
                + "\"lastName\": \"McFly\","
                + "\"phoneNumber\": \"767676767\","
                + "\"birthday\": \"1968-06-09\","
                + "\"address\": \"Calla del Olvido, 1\","
                + "\"idCard\": \"123123123X\","
                + "\"partner\": \"true\""
                + "}";

        //when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    public void testCreateUserIncorrect() throws Exception {
        Role role = new Role(RoleEnum.STUDENT);
        //when(roleService.findByName(any(RoleEnum.class))).thenReturn(role);
        //when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        User user = new User();
        user.setPassword("password");
        user.setRole(roleService.findByName(RoleEnum.STUDENT));
        user.setFirstName("Marty");
        user.setLastName("McFly");
        user.setPhoneNumber("767676767");
        user.setBirthday(LocalDate.of(1968, 6, 9));
        String userJson = "{"
                + "\"password\": \"password\","
                + "\"role\": \"STUDENT\","
                + "\"firstName\": \"Marty\","
                + "\"lastName\": \"McFly\","
                + "\"phoneNumber\": \"767676767\","
                + "\"birthday\": \"1968-06-09\""
                + "}";

        //when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Transactional
    @Test
    public void testLoginUserCorrect() throws Exception {
        Role role = new Role(RoleEnum.STUDENT);
        //when(roleService.findByName(any(RoleEnum.class))).thenReturn(role);
        //when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        User user = new User();
        user.setEmail("student@prueba.com");
        user.setPassword("password");
        user.setRole(roleService.findByName(RoleEnum.STUDENT));
        user.setFirstName("Marty");
        user.setLastName("McFly");
        user.setPhoneNumber("767676767");
        user.setBirthday(LocalDate.of(1968, 6, 9));
        String userJson = "{"
                + "\"email\": \"student@prueba.com\","
                + "\"password\": \"password\","
                + "\"role\": \"STUDENT\","
                + "\"firstName\": \"Marty\","
                + "\"lastName\": \"McFly\","
                + "\"phoneNumber\": \"767676767\","
                + "\"birthday\": \"1968-06-09\","
                + "\"address\": \"Calla del Olvido, 1\","
                + "\"idCard\": \"123123123X\","
                + "\"partner\": \"true\""
                + "}";

        //when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk());

        //when(userRepository.findByEmail("student@prueba.com")).thenReturn(Optional.of(user));

        String userLoginJson = "{"
                + "\"email\": \"student@prueba.com\","
                + "\"password\": \"password\""
                + "}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userLoginJson))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    public void testLoginUserIncorrect() throws Exception {
        Role role = new Role(RoleEnum.STUDENT);
        //when(roleService.findByName(any(RoleEnum.class))).thenReturn(role);
        //when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        User user = new User();
        user.setEmail("student@prueba.com");
        user.setPassword("password");
        user.setRole(role);
        user.setFirstName("Marty");
        user.setLastName("McFly");
        user.setPhoneNumber("767676767");
        user.setBirthday(LocalDate.of(1968, 6, 9));

        //when(userRepository.save(any(User.class))).thenReturn(user);

        String userJson = "{"
                + "\"email\": \"student@prueba.com\","
                + "\"password\": \"password\","
                + "\"role\": \"STUDENT\","
                + "\"firstName\": \"Marty\","
                + "\"lastName\": \"McFly\","
                + "\"phoneNumber\": \"767676767\","
                + "\"birthday\": \"1968-06-09\","
                + "\"address\": \"Calla del Olvido, 1\","
                + "\"idCard\": \"123123123X\","
                + "\"partner\": \"true\""
                + "}";

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk());

        //when(userRepository.findByEmail("student@prueba.com")).thenReturn(Optional.of(user));

        String userLoginJson = "{"
                + "\"email\": \"student@prueba.com\","
                + "\"password\": \"wrongpassword\""
                + "}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userLoginJson))
                .andExpect(status().isUnauthorized())
                .andExpect(result -> System.out.println(result.getResponse().getContentAsString()));
    }

    @Transactional
    @Test
    public void testLogoutUserCorrect() throws Exception {
        Role role = new Role(RoleEnum.STUDENT);
        //when(roleService.findByName(any(RoleEnum.class))).thenReturn(role);
        //when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        User user = new User();
        user.setEmail("student@prueba.com");
        user.setPassword("password");
        user.setRole(roleService.findByName(RoleEnum.STUDENT));
        user.setFirstName("Marty");
        user.setLastName("McFly");
        user.setPhoneNumber("767676767");
        user.setBirthday(LocalDate.of(1968, 6, 9));
        String userJson = "{"
                + "\"email\": \"student@prueba.com\","
                + "\"password\": \"password\","
                + "\"role\": \"STUDENT\","
                + "\"firstName\": \"Marty\","
                + "\"lastName\": \"McFly\","
                + "\"phoneNumber\": \"767676767\","
                + "\"birthday\": \"1968-06-09\","
                + "\"address\": \"Calla del Olvido, 1\","
                + "\"idCard\": \"123123123X\","
                + "\"partner\": \"true\""
                + "}";

        //when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk());

        //when(userRepository.findByEmail("student@prueba.com")).thenReturn(Optional.of(user));

        String userLoginJson = "{"
                + "\"email\": \"student@prueba.com\","
                + "\"password\": \"password\""
                + "}";

        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userLoginJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String token = extractValue(response, "token");

        mockMvc.perform(get("/auth/logout")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
