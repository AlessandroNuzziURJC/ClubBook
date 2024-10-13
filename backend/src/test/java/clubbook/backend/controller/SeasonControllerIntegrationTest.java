package clubbook.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"/scripts/roles_dataset.sql", "/scripts/dataset.sql"})
public class SeasonControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void hasStartedStudentTest() throws Exception {
        mockMvc.perform(get("/season/started"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void hasStartedTeacherTest() throws Exception {
        mockMvc.perform(get("/season/started"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "administrator", roles = {"ADMINISTRATOR"})
    void hasStartedAdministratorTest() throws Exception {
        mockMvc.perform(get("/season/started"))
                .andExpect(status().isOk());
    }

    @Test
    void hasStartedNotLoggedTest() throws Exception {
        mockMvc.perform(get("/season/started"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void startSeasonUserTest() throws Exception {
        mockMvc.perform(post("/season/start/{adminId}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void startSeasonTeacherTest() throws Exception {
        mockMvc.perform(post("/season/start/{adminId}", 62)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "administrator", roles = {"ADMINISTRATOR"})
    void startSeasonAdministratorTest() throws Exception {
        mockMvc.perform(post("/season/start/{adminId}", 72)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void startSeasonNotLoggedTest() throws Exception {
        mockMvc.perform(post("/season/start/{adminId}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void finishSeasonStudentTest() throws Exception {
        mockMvc.perform(post("/season/finish/{adminId}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void finishSeasonTeacherTest() throws Exception {
        mockMvc.perform(post("/season/finish/{adminId}", 62)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "administrator", roles = {"ADMINISTRATOR"})
    void finishSeasonAdministratorTest() throws Exception {
        mockMvc.perform(post("/season/finish/{adminId}", 72)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void finishSeasonNotLoggedTest() throws Exception {
        mockMvc.perform(post("/season/finish/{adminId}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
