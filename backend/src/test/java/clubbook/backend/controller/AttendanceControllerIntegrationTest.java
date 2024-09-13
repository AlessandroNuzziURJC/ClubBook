package clubbook.backend.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"/scripts/roles_dataset.sql", "/scripts/dataset.sql"})
public class AttendanceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Transactional
    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    public void saveAttendancesTestStudent() throws Exception {
        String jsonRequest = "{"
                + "\"date\": \"2024-09-04\","
                + "\"classGroup\": 1,"
                + "\"usersIdsAttended\": [1, 2, 3, 4, 5, 6, 8, 9, 10],"
                + "\"usersIdsNotAttended\": [7]"
                + "}";

        mockMvc.perform(post("/attendance/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    public void saveAttendancesTestTeacher() throws Exception {
        String jsonRequest = "{"
                + "\"date\": \"2024-09-04\","
                + "\"classGroup\": 1,"
                + "\"usersIdsAttended\": [1, 2, 3, 4, 5, 6, 8, 9, 10],"
                + "\"usersIdsNotAttended\": [7]"
                + "}";

        mockMvc.perform(post("/attendance/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    public void saveAttendancesTestAdministrator() throws Exception {
        String jsonRequest = "{"
                + "\"date\": \"2024-09-04\","
                + "\"classGroup\": 1,"
                + "\"usersIdsAttended\": [1, 2, 3, 4, 5, 6, 8, 9, 10],"
                + "\"usersIdsNotAttended\": [7]"
                + "}";

        mockMvc.perform(post("/attendance/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    public void saveAttendancesTesNoRole() throws Exception {
        String jsonRequest = "{"
                + "\"date\": \"2024-09-04\","
                + "\"classGroup\": 1,"
                + "\"usersIdsAttended\": [1, 2, 3, 4, 5, 6, 8, 9, 10],"
                + "\"usersIdsNotAttended\": [7]"
                + "}";

        mockMvc.perform(post("/attendance/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    public void getAttendancesTestStudent() throws Exception {
        mockMvc.perform(get("/attendance/01/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    public void getAttendancesTestTeacher() throws Exception {
        mockMvc.perform(get("/attendance/01/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    public void getAttendancesTestAdministrator() throws Exception {
        mockMvc.perform(get("/attendance/01/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    public void getAttendancesTestNoRole() throws Exception {
        mockMvc.perform(get("/attendance/01/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
