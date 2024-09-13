package clubbook.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"/scripts/roles_dataset.sql", "/scripts/dataset.sql"})
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String extractValue(String response, String key) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(response);

        JsonNode dataNode = jsonNode.get("data");

        if (dataNode != null && dataNode.has(key)) {
            return dataNode.get(key).asText();
        } else {
            throw new Exception("Not found key: " + key);
        }
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

    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void getMyUserDataStudent() throws Exception {
        mockMvc.perform(get("/{id}/me", 1))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void getMyUserDataTeacher() throws Exception {
        mockMvc.perform(get("/{id}/me", 61))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void getMyUserDataAdministrator() throws Exception {
        String output = mockMvc.perform(get("/{id}/me", 71))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        System.out.println(output);
    }

    @Test
    void getProfilePictureNotLogged() throws Exception {
        mockMvc.perform(get("/{id}/profilePicture", 1))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void getProfilePictureStudent() throws Exception {
        Path imagePath = Paths.get("src/test/resources/assets/profilepics/Profile_blue.png");
        byte[] imageBytes = Files.readAllBytes(imagePath);

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "image",
                "Profile_blue.png",
                "image/png",
                imageBytes
        );

        mockMvc.perform(multipart("/{id}/uploadProfilePicture", 1)
                        .file(mockMultipartFile))
                .andExpect(status().isOk());
        mockMvc.perform(get("/{id}/profilePicture", 1))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void getProfilePictureTeacher() throws Exception {
        Path imagePath = Paths.get("src/test/resources/assets/profilepics/Profile_blue.png");
        byte[] imageBytes = Files.readAllBytes(imagePath);

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "image",
                "Profile_blue.png",
                "image/png",
                imageBytes
        );

        mockMvc.perform(multipart("/{id}/uploadProfilePicture", 61)
                        .file(mockMultipartFile))
                .andExpect(status().isOk());

        mockMvc.perform(get("/{id}/profilePicture", 61))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void getProfilePictureAdministrator() throws Exception {
        Path imagePath = Paths.get("src/test/resources/assets/profilepics/Profile_blue.png");
        byte[] imageBytes = Files.readAllBytes(imagePath);

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "image",
                "Profile_blue.png",
                "image/png",
                imageBytes
        );

        mockMvc.perform(multipart("/{id}/uploadProfilePicture", 71)
                        .file(mockMultipartFile))
                        .andExpect(status().isOk());
        mockMvc.perform(get("/{id}/profilePicture", 71))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void getProfilePictureNotFoundAdministrator() throws Exception {
        mockMvc.perform(get("/{id}/profilePicture", 108))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "teststudent2@gmail.com", roles = {"STUDENT"})
    void updateStudentData() throws Exception {

        String userJsonUpdated = "{"
                + "\"email\": \"teststudent2@gmail.com\","
                + "\"password\": \"password2\","
                + "\"role\": \"STUDENT\","
                + "\"firstName\": \"Marty\","
                + "\"lastName\": \"McFly\","
                + "\"phoneNumber\": \"123123124\","
                + "\"birthday\": \"1968-06-09\","
                + "\"address\": \"Calle del Olvido, 1\","
                + "\"idCard\": \"123123123X\","
                + "\"partner\": \"true\""
                + "}";

        mockMvc.perform(post("/{id}/updateUser", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJsonUpdated))
                .andExpect(status().isOk());

        String response;
        response = mockMvc.perform(get("/{id}/me", 2))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertEquals("Marty", extractValue(response, "firstName"));
        assertEquals("McFly", extractValue(response, "lastName"));
        assertEquals("123123123X", extractValue(response, "idCard"));
        assertEquals("1968-06-09", extractValue(response, "birthday"));
    }

    @Test
    @WithMockUser(username = "testteacher2@gmail.com", roles = {"TEACHER"})
    void updateTeacherData() throws Exception {

        String userJsonUpdated = "{"
                + "\"email\": \"testteacher2@gmail.com\","
                + "\"password\": \"teacherpass2\","
                + "\"role\": \"TEACHER\","
                + "\"firstName\": \"Marty\","
                + "\"lastName\": \"McFly\","
                + "\"phoneNumber\": \"123123124\","
                + "\"birthday\": \"1968-06-09\","
                + "\"address\": \"Calle del Olvido, 1\","
                + "\"idCard\": \"123123123X\","
                + "\"partner\": \"true\""
                + "}";

        mockMvc.perform(post("/{id}/updateUser", 62)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJsonUpdated))
                .andExpect(status().isOk());

        String response;
        response = mockMvc.perform(get("/{id}/me", 62))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertEquals("Marty", extractValue(response, "firstName"));
        assertEquals("McFly", extractValue(response, "lastName"));
        assertEquals("123123123X", extractValue(response, "idCard"));
        assertEquals("1968-06-09", extractValue(response, "birthday"));
    }


    @Test
    @WithMockUser(username = "testadministrator2@gmail.com", roles = {"ADMINISTRATOR"})
    void updateAdministratorData() throws Exception {

        String userJsonUpdated = "{"
                + "\"email\": \"testadministrator2@gmail.com\","
                + "\"password\": \"adminpass2\","
                + "\"role\": \"ADMINISTRATOR\","
                + "\"firstName\": \"Marty\","
                + "\"lastName\": \"McFly\","
                + "\"phoneNumber\": \"123123124\","
                + "\"birthday\": \"1968-06-09\","
                + "\"address\": \"Calle del Olvido, 1\","
                + "\"idCard\": \"123123123X\","
                + "\"partner\": \"true\""
                + "}";

        mockMvc.perform(post("/{id}/updateUser", 72)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJsonUpdated))
                .andExpect(status().isOk());

        String response;
        response = mockMvc.perform(get("/{id}/me", 72))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertEquals("Marty", extractValue(response, "firstName"));
        assertEquals("McFly", extractValue(response, "lastName"));
        assertEquals("123123123X", extractValue(response, "idCard"));
        assertEquals("1968-06-09", extractValue(response, "birthday"));
    }


    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void getStudentsDataTeacher() throws Exception {
        String response;
        response = mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals("60", extractValue(response, "totalElements"));

        response = mockMvc.perform(get("/students?pageSize=2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals("30", extractValue(response, "totalPages"));
        assertEquals("2", extractValue(response, "numberOfElements"));

        response = mockMvc.perform(get("/students?pageNumber=30&pageSize=2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals("30", extractValue(response, "totalPages"));
        assertEquals("0", extractValue(response, "numberOfElements"));
    }

    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void getStudentsDataAdministrator() throws Exception {
        String response;
        response = mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals("60", extractValue(response, "totalElements"));

        response = mockMvc.perform(get("/students?pageSize=2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals("30", extractValue(response, "totalPages"));
        assertEquals("2", extractValue(response, "numberOfElements"));

        response = mockMvc.perform(get("/students?pageNumber=30&pageSize=2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals("30", extractValue(response, "totalPages"));
        assertEquals("0", extractValue(response, "numberOfElements"));
    }


    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void getStudentsDataStudent() throws Exception {

        mockMvc.perform(get("/students?pageNumber=1&pageSize=2"))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/students?pageNumber=1"))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/students?pageSize=2"))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/students"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void getTeachersDataTeacher() throws Exception {

        mockMvc.perform(get("/teachers"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/teachers?pageSize=2"))
                .andExpect(status().isForbidden());

         mockMvc.perform(get("/teachers?pageNumber=10&pageSize=2"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    void getTeachersDataAdministrator() throws Exception {

        String response;

        response = mockMvc.perform(get("/teachers"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals("10", extractValue(response, "totalElements"));

        response = mockMvc.perform(get("/teachers?pageSize=2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals("5", extractValue(response, "totalPages"));
        assertEquals("2", extractValue(response, "numberOfElements"));

        response = mockMvc.perform(get("/teachers?pageNumber=10&pageSize=2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals("5", extractValue(response, "totalPages"));
        assertEquals("0", extractValue(response, "numberOfElements"));
    }


    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void getTeachersDataStudent() throws Exception {

        mockMvc.perform(get("/teachers?pageNumber=1&pageSize=2"))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/teachers?pageNumber=1"))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/teachers?pageSize=2"))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/teachers"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getTeachersNotLogged() throws Exception {
        mockMvc.perform(get("/allTeachers")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void getTeachersStudent() throws Exception {
        mockMvc.perform(get("/allTeachers")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void getTeachersTeacher() throws Exception {
        mockMvc.perform(get("/allTeachers")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "administrator", roles = {"ADMINISTRATOR"})
    void getTeachersAdministrator() throws Exception {
        mockMvc.perform(get("/allTeachers")).andExpect(status().isOk());
    }

    @Test
    void getTeachersSearchNotLogged() throws Exception {
        mockMvc.perform(get("/teachersSearch").param("search", "Alice")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void getTeachersSearchStudent() throws Exception {
        mockMvc.perform(get("/teachersSearch").param("search", "Alice")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void getTeachersSearchTeacher() throws Exception {
        mockMvc.perform(get("/teachersSearch").param("search", "Alice")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "administrator", roles = {"ADMINISTRATOR"})
    void getTeachersSearchAdministrator() throws Exception {
        mockMvc.perform(get("/teachersSearch").param("search", "Alice")).andExpect(status().isOk());
    }

    @Test
    void getStudentsSearchNotLogged() throws Exception {
        mockMvc.perform(get("/studentsSearch").param("search", "Tina")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void getStudentsSearchStudent() throws Exception {
        mockMvc.perform(get("/studentsSearch").param("search", "Tina")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void getStudentsSearchTeacher() throws Exception {
        mockMvc.perform(get("/studentsSearch").param("search", "Tina")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "administrator", roles = {"ADMINISTRATOR"})
    void getStudentsSearchAdministrator() throws Exception {
        mockMvc.perform(get("/studentsSearch").param("search", "Tina")).andExpect(status().isOk());
    }

    @Test
    void getStudentsWithoutClassGroupNotLogged() throws Exception {
        mockMvc.perform(get("/studentsWithoutClassGroup")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void getStudentsWithoutClassGroupStudent() throws Exception {
        mockMvc.perform(get("/studentsWithoutClassGroup")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void getStudentsWithoutClassGroupTeacher() throws Exception {
        mockMvc.perform(get("/studentsWithoutClassGroup")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "administrator", roles = {"ADMINISTRATOR"})
    void getStudentsWithoutClassGroupAdministrator() throws Exception {
        mockMvc.perform(get("/studentsWithoutClassGroup")).andExpect(status().isOk());
    }
}
