package clubbook.backend.controller;

import clubbook.backend.model.NotebookEntry;
import clubbook.backend.service.NotebookService;
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
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"/scripts/roles_dataset.sql", "/scripts/dataset.sql", "/scripts/notification.sql", "/scripts/notebooks.sql"})
public class NotebookGenerationAIIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotebookService notebookService;

    private String jsonObject = "{\n" +
            "  \"warmUpExercises\": [\"Caminata ligera\",\"Saltos en el lugar\",\"Estiramientos de brazos\"],\n" +
            "  \"specificExercises\": [\"Tecnica de caida (Ukemi)\",\"Posturas fundamentales (Kamae)\",\"Movimientos de piernas\"],\n" +
            "  \"finalExercises\": [\"Juego de equipo\",\"Estiramientos basicos\"]\n" +
            "}\n";

    private String output = "{\"message\":\"\",\"data\":{\"id\":0,\"warmUpExercises\":[\"Caminata ligera\",\"Saltos en el lugar\",\"Estiramientos de brazos\"],\"specificExercises\":[\"Tecnica de caida (Ukemi)\",\"Posturas fundamentales (Kamae)\",\"Movimientos de piernas\"],\"finalExercises\":[\"Juego de equipo\",\"Estiramientos basicos\"],\"date\":\"2024-11-03\"}}";


    @Transactional
    @Test
    void generateEntryNotLogged() throws Exception {
        NotebookEntry mockEntry = new NotebookEntry(jsonObject, LocalDate.now());
        Mockito.when(notebookService.generateEntry(Mockito.eq(10), Mockito.any(LocalDate.class))).thenReturn(mockEntry);

        mockMvc.perform(get("/notebook/generateEntry/{id}", 10)
                        .param("date", LocalDate.now().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void generateEntryStudent() throws Exception {
        NotebookEntry mockEntry = new NotebookEntry(jsonObject, LocalDate.now());
        Mockito.when(notebookService.generateEntry(Mockito.eq(10), Mockito.any(LocalDate.class))).thenReturn(mockEntry);

        mockMvc.perform(get("/notebook/generateEntry/{id}", 10)
                        .param("date", LocalDate.now().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void generateEntryTeacher() throws Exception {
        NotebookEntry mockEntry = new NotebookEntry(jsonObject, LocalDate.now());
        Mockito.when(notebookService.generateEntry(Mockito.eq(10), Mockito.any(LocalDate.class))).thenReturn(mockEntry);

        ResultActions resultActions = mockMvc.perform(get("/notebook/generateEntry/{id}", 10)
                        .param("date", LocalDate.now().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        String content = resultActions.andReturn().getResponse().getContentAsString();

        assertEquals(output, content);
    }

    @Transactional
    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void generateEntryAdministrator() throws Exception {
        NotebookEntry mockEntry = new NotebookEntry(jsonObject, LocalDate.now());
        Mockito.when(notebookService.generateEntry(Mockito.eq(10), Mockito.any(LocalDate.class))).thenReturn(mockEntry);

        mockMvc.perform(get("/notebook/generateEntry/{id}", 10)
                        .param("date", LocalDate.now().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
