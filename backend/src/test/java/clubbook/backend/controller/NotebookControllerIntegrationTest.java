package clubbook.backend.controller;

import clubbook.backend.dtos.NotebookEntryDto;
import clubbook.backend.dtos.NotebookUpdateConfigDto;
import clubbook.backend.model.Notebook;
import clubbook.backend.model.NotebookEntry;
import clubbook.backend.responses.ResponseMessages;
import clubbook.backend.service.NotebookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"/scripts/roles_dataset.sql", "/scripts/dataset.sql", "/scripts/notification.sql", "/scripts/notebooks.sql"})
class NotebookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Transactional
    @Test
    void getAllClassGroupsNotLogged() throws Exception {
        mockMvc.perform(get("/notebook/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void getAllClassGroupsStudent() throws Exception {
        mockMvc.perform(get("/notebook/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void getAllClassGroupsTeacher() throws Exception {
        mockMvc.perform(get("/notebook/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void getAllClassGroupsAdministrator() throws Exception {
        mockMvc.perform(get("/notebook/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    void getNotebookByIdNotLogged() throws Exception {
        mockMvc.perform(get("/notebook/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void getNotebookByIdStudent() throws Exception {
        mockMvc.perform(get("/notebook/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void getNotebookByIdTeacher() throws Exception {
        mockMvc.perform(get("/notebook/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void getNotebookByIdAdministrator() throws Exception {
        mockMvc.perform(get("/notebook/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    void getNotebookEntryTodayByIdNotLogged() throws Exception {
        mockMvc.perform(get("/notebook/entry/today/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void getNotebookEntryTodayByIdStudent() throws Exception {
        mockMvc.perform(get("/notebook/entry/today/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void getNotebookEntryTodayByIdTeacher() throws Exception {
        mockMvc.perform(get("/notebook/entry/today/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void getNotebookEntryTodayByIdAdministrator() throws Exception {
        mockMvc.perform(get("/notebook/entry/today/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    void updateNotebookConfigNotLogged() throws Exception {
        NotebookUpdateConfigDto notebookUpdateConfigDto = new NotebookUpdateConfigDto();
        notebookUpdateConfigDto.setLevel("Intermedio");
        notebookUpdateConfigDto.setId(10);
        notebookUpdateConfigDto.setSport("Tiro con arco");

        ObjectMapper objectMapper = new ObjectMapper();
        String notebookUpdateConfigDtoJson = objectMapper.writeValueAsString(notebookUpdateConfigDto);

        mockMvc.perform(put("/notebook/config")
                .contentType(MediaType.APPLICATION_JSON)
                .content(notebookUpdateConfigDtoJson))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void updateNotebookConfigStudent() throws Exception {
        NotebookUpdateConfigDto notebookUpdateConfigDto = new NotebookUpdateConfigDto();
        notebookUpdateConfigDto.setLevel("Intermedio");
        notebookUpdateConfigDto.setId(10);
        notebookUpdateConfigDto.setSport("Tiro con arco");

        ObjectMapper objectMapper = new ObjectMapper();
        String notebookUpdateConfigDtoJson = objectMapper.writeValueAsString(notebookUpdateConfigDto);

        mockMvc.perform(put("/notebook/config")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(notebookUpdateConfigDtoJson))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void updateNotebookConfigTeacher() throws Exception {
        NotebookUpdateConfigDto notebookUpdateConfigDto = new NotebookUpdateConfigDto();
        notebookUpdateConfigDto.setLevel("Intermedio");
        notebookUpdateConfigDto.setId(10);
        notebookUpdateConfigDto.setSport("Tiro con arco");

        ObjectMapper objectMapper = new ObjectMapper();
        String notebookUpdateConfigDtoJson = objectMapper.writeValueAsString(notebookUpdateConfigDto);

        mockMvc.perform(put("/notebook/config")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(notebookUpdateConfigDtoJson))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void updateNotebookConfigAdministrator() throws Exception {
        NotebookUpdateConfigDto notebookUpdateConfigDto = new NotebookUpdateConfigDto();
        notebookUpdateConfigDto.setLevel("Intermedio");
        notebookUpdateConfigDto.setId(10);
        notebookUpdateConfigDto.setSport("Tiro con arco");

        ObjectMapper objectMapper = new ObjectMapper();
        String notebookUpdateConfigDtoJson = objectMapper.writeValueAsString(notebookUpdateConfigDto);

        mockMvc.perform(put("/notebook/config")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(notebookUpdateConfigDtoJson))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    void addNotebookEntryNotLogged() throws Exception {
        NotebookEntryDto notebookEntryDto = new NotebookEntryDto();
        notebookEntryDto.setWarmUpExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        notebookEntryDto.setSpecificExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        notebookEntryDto.setFinalExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        int notebookId = 10;

        ObjectMapper objectMapper = new ObjectMapper();
        String notebookEntryDtoJson = objectMapper.writeValueAsString(notebookEntryDto);

        mockMvc.perform(post("/notebook/entry/{id}", notebookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(notebookEntryDtoJson))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void addNotebookEntryStudent() throws Exception {
        NotebookEntryDto notebookEntryDto = new NotebookEntryDto();
        notebookEntryDto.setWarmUpExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        notebookEntryDto.setSpecificExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        notebookEntryDto.setFinalExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        int notebookId = 10;

        ObjectMapper objectMapper = new ObjectMapper();
        String notebookEntryDtoJson = objectMapper.writeValueAsString(notebookEntryDto);

        mockMvc.perform(post("/notebook/entry/{id}", notebookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(notebookEntryDtoJson))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void addNotebookEntryTeacher() throws Exception {
        NotebookEntryDto notebookEntryDto = new NotebookEntryDto();
        notebookEntryDto.setWarmUpExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        notebookEntryDto.setSpecificExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        notebookEntryDto.setFinalExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        int notebookId = 10;

        ObjectMapper objectMapper = new ObjectMapper();
        String notebookEntryDtoJson = objectMapper.writeValueAsString(notebookEntryDto);

        mockMvc.perform(post("/notebook/entry/{id}", notebookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(notebookEntryDtoJson))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void addNotebookEntryAdministrator() throws Exception {
        NotebookEntryDto notebookEntryDto = new NotebookEntryDto();
        notebookEntryDto.setWarmUpExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        notebookEntryDto.setSpecificExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        notebookEntryDto.setFinalExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        int notebookId = 10;

        ObjectMapper objectMapper = new ObjectMapper();
        String notebookEntryDtoJson = objectMapper.writeValueAsString(notebookEntryDto);

        mockMvc.perform(post("/notebook/entry/{id}", notebookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(notebookEntryDtoJson))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    void getEntriesPagedNotLogged() throws Exception {
        mockMvc.perform(get("/notebook/entry/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void getEntriesPagedStudent() throws Exception {
        mockMvc.perform(get("/notebook/entry/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void getEntriesPagedTeacher() throws Exception {
        mockMvc.perform(get("/notebook/entry/{id}?pageNumber=0", 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.numberOfElements").value(10));

    }

    @Transactional
    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void getEntriesPagedAdministrator() throws Exception {
        mockMvc.perform(get("/notebook/entry/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    void deleteNotebookEntryNotLogged() throws Exception {
        mockMvc.perform(delete("/notebook/entry/{id}", 106)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void deleteNotebookEntryStudent() throws Exception {
        mockMvc.perform(delete("/notebook/entry/{id}", 106)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void deleteNotebookEntryTeacher() throws Exception {
        mockMvc.perform(delete("/notebook/entry/{id}", 106)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void deleteNotebookEntryAdministrator() throws Exception {
        mockMvc.perform(delete("/notebook/entry/{id}", 106)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    void editNotebookEntryNotLogged() throws Exception {
        NotebookEntry notebookEntry = new NotebookEntry();
        notebookEntry.setId(106);
        notebookEntry.setWarmUpExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        notebookEntry.setSpecificExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        notebookEntry.setFinalExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        notebookEntry.setDate(LocalDate.now().plusDays(5));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String notebookEntryJson = objectMapper.writeValueAsString(notebookEntry);

        mockMvc.perform(put("/notebook/entry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(notebookEntryJson))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void editNotebookEntryStudent() throws Exception {
        NotebookEntry notebookEntry = new NotebookEntry();
        notebookEntry.setId(106);
        notebookEntry.setWarmUpExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        notebookEntry.setSpecificExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        notebookEntry.setFinalExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        notebookEntry.setDate(LocalDate.now().plusDays(5));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String notebookEntryJson = objectMapper.writeValueAsString(notebookEntry);

        mockMvc.perform(put("/notebook/entry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(notebookEntryJson))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void editNotebookEntryTeacher() throws Exception {
        NotebookEntry notebookEntry = new NotebookEntry();
        notebookEntry.setId(106);
        notebookEntry.setWarmUpExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        notebookEntry.setSpecificExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        notebookEntry.setFinalExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        notebookEntry.setDate(LocalDate.now().plusDays(5));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String notebookEntryJson = objectMapper.writeValueAsString(notebookEntry);

        mockMvc.perform(put("/notebook/entry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(notebookEntryJson))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void editNotebookEntryAdministrator() throws Exception {
        NotebookEntry notebookEntry = new NotebookEntry();
        notebookEntry.setId(106);
        notebookEntry.setWarmUpExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        notebookEntry.setSpecificExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        notebookEntry.setFinalExercises(new ArrayList<>(Arrays.asList("A", "B", "C")));
        notebookEntry.setDate(LocalDate.now().plusDays(5));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String notebookEntryJson = objectMapper.writeValueAsString(notebookEntry);

        mockMvc.perform(put("/notebook/entry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(notebookEntryJson))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    void getInvalidDatesNotLogged() throws Exception {
        mockMvc.perform(get("/notebook/invalidDates/{id}", 10)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void getInvalidDatesStudent() throws Exception {
        mockMvc.perform(get("/notebook/invalidDates/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void getInvalidDatesTeacher() throws Exception {
        mockMvc.perform(get("/notebook/invalidDates/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(11));
    }

    @Transactional
    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void getInvalidDatesAdministrator() throws Exception {
        mockMvc.perform(get("/notebook/invalidDates/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}