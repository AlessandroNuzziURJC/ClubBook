package clubbook.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.test.annotation.DirtiesContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private String extractValue(String response, String key) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get(key).asText();
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

        String userId = extractValue(response, "id");

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

        String userId = extractValue(response, "id");

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

        String userId = extractValue(response, "id");

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

        String userId = extractValue(response, "id");

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

        String userId = extractValue(response, "id");

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

        String userId = extractValue(response, "id");

        mockMvc.perform(get("/{id}/profilePicture", userId))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    @WithMockUser(username = "student@prueba.com", roles = {"STUDENT"})
    void updateStudentData() throws Exception {
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

        String userId = extractValue(response, "id");

        String userLoginJson = "{"
                + "\"email\": \"student@prueba.com\","
                + "\"password\": \"password\"}";

        response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userLoginJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String token = extractValue(response, "token");

        String userJsonUpdated = "{"
                + "\"email\": \"student@prueba.com\","
                + "\"password\": \"password\","
                + "\"role\": \"STUDENT\","
                + "\"firstName\": \"Marty\","
                + "\"lastName\": \"McFly\","
                + "\"phoneNumber\": \"123456789\","
                + "\"birthday\": \"1968-06-09\","
                + "\"address\": \"Calla del Olvido, 1\","
                + "\"idCard\": \"123123123X\","
                + "\"partner\": \"true\""
                + "}";

        mockMvc.perform(post("/{id}/updateUser", userId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJsonUpdated))
                .andExpect(status().isOk());

        mockMvc.perform(get("/{id}/me", userId))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    @WithMockUser(username = "teacher@prueba.com", roles = {"TEACHER"})
    void updateTeacherData() throws Exception {
        String userJson = "{"
                + "\"email\": \"teacher@prueba.com\","
                + "\"password\": \"password\","
                + "\"role\": \"TEACHER\","
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

        String userId = extractValue(response, "id");

        String userLoginJson = "{"
                + "\"email\": \"teacher@prueba.com\","
                + "\"password\": \"password\"}";

        response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userLoginJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String token = extractValue(response, "token");

        String userJsonUpdated = "{"
                + "\"email\": \"teacher@prueba.com\","
                + "\"password\": \"password\","
                + "\"role\": \"TEACHER\","
                + "\"firstName\": \"Marty\","
                + "\"lastName\": \"McFly\","
                + "\"phoneNumber\": \"123456789\","
                + "\"birthday\": \"1968-06-09\","
                + "\"address\": \"Calla del Olvido, 1\","
                + "\"idCard\": \"123123123X\","
                + "\"partner\": \"true\""
                + "}";

        mockMvc.perform(post("/{id}/updateUser", userId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJsonUpdated))
                .andExpect(status().isOk());

        mockMvc.perform(get("/{id}/me", userId))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    @WithMockUser(username = "administrator@prueba.com", roles = {"ADMINISTATOR"})
    void updateAdministratorData() throws Exception {
        String userJson = "{"
                + "\"email\": \"administrator@prueba.com\","
                + "\"password\": \"password\","
                + "\"role\": \"ADMINISTRATOR\","
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

        String userId = extractValue(response, "id");

        String userLoginJson = "{"
                + "\"email\": \"administrator@prueba.com\","
                + "\"password\": \"password\"}";

        response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userLoginJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String token = extractValue(response, "token");

        String userJsonUpdated = "{"
                + "\"email\": \"administrator@prueba.com\","
                + "\"password\": \"password\","
                + "\"role\": \"ADMINISTRATOR\","
                + "\"firstName\": \"Marty\","
                + "\"lastName\": \"McFly\","
                + "\"phoneNumber\": \"123456789\","
                + "\"birthday\": \"1968-06-09\","
                + "\"address\": \"Calla del Olvido, 1\","
                + "\"idCard\": \"123123123X\","
                + "\"partner\": \"true\""
                + "}";

        mockMvc.perform(post("/{id}/updateUser", userId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJsonUpdated))
                .andExpect(status().isOk());

        mockMvc.perform(get("/{id}/me", userId))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void getStudentsDataTeacher() throws Exception {

        String response;

        String student1 = "{"
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
                        .content(student1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


        String student2 = "{"
                + "\"email\": \"student2@prueba.com\","
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
                        .content(student2))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String student3 = "{"
                + "\"email\": \"student3@prueba.com\","
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
                        .content(student3))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        response = mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals("3", extractValue(response, "totalElements"));

        response = mockMvc.perform(get("/students?pageSize=2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals("2", extractValue(response, "totalPages"));
        assertEquals("2", extractValue(response, "numberOfElements"));

        response = mockMvc.perform(get("/students?pageNumber=1&pageSize=2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals("2", extractValue(response, "totalPages"));
        assertEquals("1", extractValue(response, "numberOfElements"));
    }

    @Transactional
    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    void getStudentsDataAdministrator() throws Exception {

        String response;

        String student1 = "{"
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
                        .content(student1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


        String student2 = "{"
                + "\"email\": \"student2@prueba.com\","
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
                        .content(student2))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String student3 = "{"
                + "\"email\": \"student3@prueba.com\","
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
                        .content(student3))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        response = mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals("3", extractValue(response, "totalElements"));

        response = mockMvc.perform(get("/students?pageSize=2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals("2", extractValue(response, "totalPages"));
        assertEquals("2", extractValue(response, "numberOfElements"));

        response = mockMvc.perform(get("/students?pageNumber=1&pageSize=2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals("2", extractValue(response, "totalPages"));
        assertEquals("1", extractValue(response, "numberOfElements"));
    }


    @Transactional
    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void getStudentsDataStudent() throws Exception {

        String student1 = "{"
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
                        .content(student1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


        String student2 = "{"
                + "\"email\": \"student2@prueba.com\","
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
                        .content(student2))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String student3 = "{"
                + "\"email\": \"student3@prueba.com\","
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
                        .content(student3))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        mockMvc.perform(get("/students?pageNumber=1&pageSize=2"))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/students?pageNumber=1"))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/students?pageSize=2"))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/students"))
                .andExpect(status().isForbidden());
    }
}
