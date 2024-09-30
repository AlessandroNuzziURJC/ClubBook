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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"/scripts/roles_dataset.sql", "/scripts/dataset.sql"})
public class ClassGroupControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllClassGroups_NotLogged() throws Exception {
        mockMvc.perform(get("/classGroup"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void getAllClassGroups_Student() throws Exception {
        mockMvc.perform(get("/classGroup"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void getAllClassGroups_Teacher() throws Exception {
        mockMvc.perform(get("/classGroup"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void getAllClassGroups_Administrator() throws Exception {
        mockMvc.perform(get("/classGroup"))
                .andExpect(status().isOk());
    }

    @Test
    void getClassGroup_NotLogged() throws Exception {
        mockMvc.perform(get("/{id}/classGroup", 1))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void getClassGroup_Student() throws Exception {
        mockMvc.perform(get("/{id}/classGroup", 1))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void getClassGroup_Teacher() throws Exception {
        mockMvc.perform(get("/{id}/classGroup", 1))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void getClassGroup_Administrator() throws Exception {
        mockMvc.perform(get("/{id}/classGroup", 1))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void getClassGroup_AdministratorNotFound() throws Exception {
        mockMvc.perform(get("/{id}/classGroup", 913))
                .andExpect(status().isNotFound());
    }

    @Test
    void createClassGroup_NotLogged() throws Exception {
        String jsonRequest = "{" +
                "    \"name\": \"Ejemplo\",\n" +
                "    \"address\": \"Calle Ejemplo\",\n" +
                "    \"idTeachers\": [\n" +
                "        61,\n" +
                "        62,\n" +
                "        63\n" +
                "    ],\n" +
                "    \"schedules\": [\n" +
                "        {\n" +
                "            \"weekDay\": \"MONDAY\",\n" +
                "            \"init\": \"18:00\",\n" +
                "            \"duration\": \"1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"weekDay\": \"WEDNESDAY\",\n" +
                "            \"init\": \"18:00\",\n" +
                "            \"duration\": \"1\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        mockMvc.perform(post("/classGroup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void createClassGroup_Student() throws Exception {
        String jsonRequest = "{" +
                "    \"name\": \"Ejemplo\",\n" +
                "    \"address\": \"Calle Ejemplo\",\n" +
                "    \"idTeachers\": [\n" +
                "        61,\n" +
                "        62,\n" +
                "        63\n" +
                "    ],\n" +
                "    \"schedules\": [\n" +
                "        {\n" +
                "            \"weekDay\": \"MONDAY\",\n" +
                "            \"init\": \"18:00\",\n" +
                "            \"duration\": \"1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"weekDay\": \"WEDNESDAY\",\n" +
                "            \"init\": \"18:00\",\n" +
                "            \"duration\": \"1\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        mockMvc.perform(post("/classGroup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void createClassGroup_Teacher() throws Exception {
        String jsonRequest = "{" +
                "    \"name\": \"Ejemplo\",\n" +
                "    \"address\": \"Calle Ejemplo\",\n" +
                "    \"idTeachers\": [\n" +
                "        61,\n" +
                "        62,\n" +
                "        63\n" +
                "    ],\n" +
                "    \"schedules\": [\n" +
                "        {\n" +
                "            \"weekDay\": \"MONDAY\",\n" +
                "            \"init\": \"18:00\",\n" +
                "            \"duration\": \"1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"weekDay\": \"WEDNESDAY\",\n" +
                "            \"init\": \"18:00\",\n" +
                "            \"duration\": \"1\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        mockMvc.perform(post("/classGroup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void createClassGroup_Administrator() throws Exception {
        mockMvc.perform(delete("/1/classGroup"))
                .andExpect(status().isOk());
        String jsonRequest = "{" +
                "    \"name\": \"Ejemplo\",\n" +
                "    \"address\": \"Calle Ejemplo\",\n" +
                "    \"teachers\": [\n" +
                "        61,\n" +
                "        62,\n" +
                "        63\n" +
                "    ],\n" +
                "    \"schedules\": [\n" +
                "        {\n" +
                "            \"weekDay\": \"MONDAY\",\n" +
                "            \"init\": \"18:00\",\n" +
                "            \"duration\": \"1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"weekDay\": \"WEDNESDAY\",\n" +
                "            \"init\": \"18:00\",\n" +
                "            \"duration\": \"1\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        mockMvc.perform(post("/classGroup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void modifyClassGroup_NotLogged() throws Exception {
        String jsonRequest = "{" +
                "    \"name\": \"Ejemplo\",\n" +
                "    \"address\": \"Calle Ejemplo\",\n" +
                "    \"teachers\": [\n" +
                "        61,\n" +
                "        62,\n" +
                "        63\n" +
                "    ],\n" +
                "    \"schedules\": [\n" +
                "        {\n" +
                "            \"weekDay\": \"MONDAY\",\n" +
                "            \"init\": \"18:00\",\n" +
                "            \"duration\": \"1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"weekDay\": \"WEDNESDAY\",\n" +
                "            \"init\": \"18:00\",\n" +
                "            \"duration\": \"1\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        mockMvc.perform(put("/1/classGroup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void modifyClassGroup_Student() throws Exception {
        String jsonRequest = "{" +
                "    \"name\": \"Ejemplo\",\n" +
                "    \"address\": \"Calle Ejemplo\",\n" +
                "    \"teachers\": [\n" +
                "        61,\n" +
                "        62,\n" +
                "        63\n" +
                "    ],\n" +
                "    \"schedules\": [\n" +
                "        {\n" +
                "            \"weekDay\": \"MONDAY\",\n" +
                "            \"init\": \"18:00\",\n" +
                "            \"duration\": \"1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"weekDay\": \"WEDNESDAY\",\n" +
                "            \"init\": \"18:00\",\n" +
                "            \"duration\": \"1\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        mockMvc.perform(put("/1/classGroup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void modifyClassGroup_Teacher() throws Exception {
        String jsonRequest = "{" +
                "    \"name\": \"Ejemplo\",\n" +
                "    \"address\": \"Calle Ejemplo\",\n" +
                "    \"teachers\": [\n" +
                "        61,\n" +
                "        62,\n" +
                "        63\n" +
                "    ],\n" +
                "    \"schedules\": [\n" +
                "        {\n" +
                "            \"weekDay\": \"MONDAY\",\n" +
                "            \"init\": \"18:00\",\n" +
                "            \"duration\": \"1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"weekDay\": \"WEDNESDAY\",\n" +
                "            \"init\": \"18:00\",\n" +
                "            \"duration\": \"1\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        mockMvc.perform(put("/1/classGroup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void modifyClassGroup_Administrator() throws Exception {
        String jsonRequest = "{" +
                "    \"name\": \"Ejemplo\",\n" +
                "    \"address\": \"Calle Ejemplo\",\n" +
                "    \"teachers\": [\n" +
                "        61,\n" +
                "        62,\n" +
                "        63\n" +
                "    ],\n" +
                "    \"schedules\": [\n" +
                "        {\n" +
                "            \"weekDay\": \"MONDAY\",\n" +
                "            \"init\": \"18:00\",\n" +
                "            \"duration\": \"1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"weekDay\": \"WEDNESDAY\",\n" +
                "            \"init\": \"18:00\",\n" +
                "            \"duration\": \"1\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        mockMvc.perform(put("/1/classGroup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void deleteClassGroup_NotLogged() throws Exception {
        mockMvc.perform(delete("/1/classGroup"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void deleteClassGroup_Student() throws Exception {
        mockMvc.perform(delete("/1/classGroup"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void deleteClassGroup_Teacher() throws Exception {
        mockMvc.perform(delete("/1/classGroup"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void deleteClassGroup_Administrator() throws Exception {
        mockMvc.perform(delete("/1/classGroup"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void deleteClassGroup_AdministratorNotFound() throws Exception {
        mockMvc.perform(delete("/913/classGroup"))
                .andExpect(status().isOk());
    }

    @Test
    void addNewStudentsClassGroup_NotLogged() throws Exception {
        String jsonRequest = "[51, 52, 53]";
        mockMvc.perform(post("/1/addStudents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void addNewStudentsClassGroup_Student() throws Exception {
        String jsonRequest = "[51, 52, 53]";
        mockMvc.perform(post("/1/addStudents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void addNewStudentsClassGroup_Teacher() throws Exception {
        String jsonRequest = "[51, 52, 53]";
        mockMvc.perform(post("/1/addStudents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void addNewStudentsClassGroup_Administrator() throws Exception {
        String jsonRequest = "[51, 52, 53]";
        mockMvc.perform(post("/1/addStudents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void addNewStudentsClassGroup_AdministratorClassGroupNotFound() throws Exception {
        String jsonRequest = "[51, 52, 53]";
        mockMvc.perform(post("/913/addStudents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void addNewStudentsClassGroup_AdministratorStudentNotFound() throws Exception {
        String jsonRequest = "[51, 52, 1923]";
        mockMvc.perform(post("/913/addStudents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

}
