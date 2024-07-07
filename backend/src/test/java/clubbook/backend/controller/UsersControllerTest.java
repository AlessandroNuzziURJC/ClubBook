package clubbook.backend.controller;

import clubbook.backend.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.annotation.DirtiesContext;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long extractIdFromResponse(String response) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get("id").asLong();
    }

    @Test
    void getAllUsersNotLogged() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void getAllUsersStudent() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void getAllUsersTeacher() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "administrator", roles = {"ADMINISTRATOR"})
    void getAllUsersAdministrator() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    void getMyUserDataNotLogged() throws Exception {
        mockMvc.perform(get("/{id}/me", 1))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void getMyUserDataStudent() throws Exception {
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

        String response = mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long userId = extractIdFromResponse(response);

        mockMvc.perform(get("/{id}/me", userId))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void getMyUserDataTeacher() throws Exception {
        String userJson = "{"
                + "\"email\": \"teacher@prueba.com\","
                + "\"password\": \"password\","
                + "\"role\": \"TEACHER\","
                + "\"firstName\": \"Doc\","
                + "\"lastName\": \"Brown\","
                + "\"phoneNumber\": \"123456789\","
                + "\"birthday\": \"1955-01-01\","
                + "\"address\": \"Main St, 1\","
                + "\"idCard\": \"987654321X\","
                + "\"partner\": \"true\""
                + "}";

        String response = mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long userId = extractIdFromResponse(response);

        mockMvc.perform(get("/{id}/me", userId))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    @WithMockUser(username = "administrator", roles = {"ADMINISTRATOR"})
    void getMyUserDataAdministrator() throws Exception {
        String userJson = "{"
                + "\"email\": \"admin@prueba.com\","
                + "\"password\": \"password\","
                + "\"role\": \"ADMINISTRATOR\","
                + "\"firstName\": \"Admin\","
                + "\"lastName\": \"Istrator\","
                + "\"phoneNumber\": \"987654321\","
                + "\"birthday\": \"1960-01-01\","
                + "\"address\": \"Admin St, 1\","
                + "\"idCard\": \"1122334455\","
                + "\"partner\": \"true\""
                + "}";

        String response = mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long userId = extractIdFromResponse(response);

        mockMvc.perform(get("/{id}/me", userId))
                .andExpect(status().isOk());
    }

    @Test
    void getProfilePictureNotLogged() throws Exception {
        mockMvc.perform(get("/{id}/profilePicture", 1))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void getProfilePictureStudent() throws Exception {
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

        String response = mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long userId = extractIdFromResponse(response);

        mockMvc.perform(get("/{id}/profilePicture", userId))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void getProfilePictureTeacher() throws Exception {
        String userJson = "{"
                + "\"email\": \"teacher@prueba.com\","
                + "\"password\": \"password\","
                + "\"role\": \"TEACHER\","
                + "\"firstName\": \"Doc\","
                + "\"lastName\": \"Brown\","
                + "\"phoneNumber\": \"123456789\","
                + "\"birthday\": \"1955-01-01\","
                + "\"address\": \"Main St, 1\","
                + "\"idCard\": \"987654321X\","
                + "\"partner\": \"true\""
                + "}";

        String response = mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long userId = extractIdFromResponse(response);

        mockMvc.perform(get("/{id}/profilePicture", userId))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    @WithMockUser(username = "administrator", roles = {"ADMINISTRATOR"})
    void getProfilePictureAdministrator() throws Exception {
        String userJson = "{"
                + "\"email\": \"admin@prueba.com\","
                + "\"password\": \"password\","
                + "\"role\": \"ADMINISTRATOR\","
                + "\"firstName\": \"Admin\","
                + "\"lastName\": \"Istrator\","
                + "\"phoneNumber\": \"987654321\","
                + "\"birthday\": \"1960-01-01\","
                + "\"address\": \"Admin St, 1\","
                + "\"idCard\": \"1122334455\","
                + "\"partner\": \"true\""
                + "}";

        String response = mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long userId = extractIdFromResponse(response);

        mockMvc.perform(get("/{id}/profilePicture", userId))
                .andExpect(status().isOk());
    }
}
