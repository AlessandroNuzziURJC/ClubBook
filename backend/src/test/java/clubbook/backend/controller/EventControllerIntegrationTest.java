package clubbook.backend.controller;

import clubbook.backend.dtos.EventDto;
import clubbook.backend.dtos.NewEventDto;
import clubbook.backend.model.EventType;
import clubbook.backend.model.enumClasses.EventTypeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"/scripts/roles_dataset.sql", "/scripts/dataset.sql", "/scripts/events.sql"})
class EventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getEventTypes_NotLogged() throws Exception {
        mockMvc.perform(get("/event/types"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void getEventTypes_Student() throws Exception {
        mockMvc.perform(get("/event/types"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void getEventTypes_Teacher() throws Exception {
        mockMvc.perform(get("/event/types"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testadminsitrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void getEventTypes_Administrator() throws Exception {
        mockMvc.perform(get("/event/types"))
                .andExpect(status().isOk());
    }

    @Test
    void saveNewEvent_NotLogged() throws Exception {
        NewEventDto newEventDto = new NewEventDto();
        newEventDto.setTitle("Test Title");
        newEventDto.setAddress("Test Address");
        newEventDto.setDate(LocalDate.now().plusDays(20));
        newEventDto.setDeadline(LocalDate.now().plusDays(10));
        newEventDto.setType(2);
        newEventDto.setBirthYearStart(LocalDate.of(2000, 1, 1));
        newEventDto.setBirthYearEnd(LocalDate.of(2010, 12, 31));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String newEventDtoJson = objectMapper.writeValueAsString(newEventDto);

        mockMvc.perform(post("/event/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newEventDtoJson))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void saveNewEvent_Student() throws Exception {
        NewEventDto newEventDto = new NewEventDto();
        newEventDto.setTitle("Test Title");
        newEventDto.setAddress("Test Address");
        newEventDto.setDate(LocalDate.now().plusDays(20));
        newEventDto.setDeadline(LocalDate.now().plusDays(10));
        newEventDto.setType(2);
        newEventDto.setBirthYearStart(LocalDate.of(2000, 1, 1));
        newEventDto.setBirthYearEnd(LocalDate.of(2010, 12, 31));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String newEventDtoJson = objectMapper.writeValueAsString(newEventDto);

        mockMvc.perform(post("/event/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newEventDtoJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void saveNewEvent_Teacher() throws Exception {
        NewEventDto newEventDto = new NewEventDto();
        newEventDto.setTitle("Test Title");
        newEventDto.setAddress("Test Address");
        newEventDto.setDate(LocalDate.now().plusDays(20));
        newEventDto.setDeadline(LocalDate.now().plusDays(10));
        newEventDto.setType(2);
        newEventDto.setBirthYearStart(LocalDate.of(2000, 1, 1));
        newEventDto.setBirthYearEnd(LocalDate.of(2010, 12, 31));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String newEventDtoJson = objectMapper.writeValueAsString(newEventDto);

        mockMvc.perform(post("/event/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newEventDtoJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testadmin1@gmail.com", roles = {"ADMINISTRATOR"})
    void saveNewEvent_Administrator() throws Exception {
        NewEventDto newEventDto = new NewEventDto();
        newEventDto.setTitle("Test Title");
        newEventDto.setAddress("Test Address");
        newEventDto.setDate(LocalDate.now().plusDays(20));
        newEventDto.setDeadline(LocalDate.now().plusDays(10));
        newEventDto.setType(2);
        newEventDto.setBirthYearStart(LocalDate.of(2000, 1, 1));
        newEventDto.setBirthYearEnd(LocalDate.of(2010, 12, 31));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String newEventDtoJson = objectMapper.writeValueAsString(newEventDto);

        mockMvc.perform(post("/event/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newEventDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    void editEvent_NotLogged() throws Exception {
        EventDto editEventDto = new EventDto();
        editEventDto.setId(106);
        editEventDto.setTitle("Test Title");
        editEventDto.setAddress("Test Address");
        editEventDto.setDate(LocalDate.now().plusDays(20));
        editEventDto.setDeadline(LocalDate.now().plusDays(10));
        editEventDto.setAdditionalInfo("Test Additional");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String editEventDtoJson = objectMapper.writeValueAsString(editEventDto);

        mockMvc.perform(put("/event/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(editEventDtoJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void editEvent_Student() throws Exception {
        EventDto editEventDto = new EventDto();
        editEventDto.setId(106);
        editEventDto.setTitle("Test Title");
        editEventDto.setAddress("Test Address");
        editEventDto.setDate(LocalDate.now().plusDays(20));
        editEventDto.setDeadline(LocalDate.now().plusDays(10));
        editEventDto.setAdditionalInfo("Test Additional");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String editEventDtoJson = objectMapper.writeValueAsString(editEventDto);

        mockMvc.perform(put("/event/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editEventDtoJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void editEvent_Teacher() throws Exception {
        EventDto editEventDto = new EventDto();
        editEventDto.setId(106);
        editEventDto.setTitle("Test Title");
        editEventDto.setAddress("Test Address");
        editEventDto.setDate(LocalDate.now().plusDays(20));
        editEventDto.setDeadline(LocalDate.now().plusDays(10));
        editEventDto.setAdditionalInfo("Test Additional");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String editEventDtoJson = objectMapper.writeValueAsString(editEventDto);

        mockMvc.perform(put("/event/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editEventDtoJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testadmin1@gmail.com", roles = {"ADMINISTRATOR"})
    void editEvent_Administrator() throws Exception {
        EventDto editEventDto = new EventDto();
        editEventDto.setId(106);
        editEventDto.setTitle("Test Title");
        editEventDto.setAddress("Test Address");
        editEventDto.setDate(LocalDate.now().plusDays(20));
        editEventDto.setDeadline(LocalDate.now().plusDays(10));
        editEventDto.setAdditionalInfo("Test Additional");
        editEventDto.setBirthYearStart(LocalDate.of(2000, 1, 1));
        editEventDto.setBirthYearEnd(LocalDate.of(2010, 12, 31));
        editEventDto.setType(new EventType(2, EventTypeEnum.EXHIBITION, new Date()));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String editEventDtoJson = objectMapper.writeValueAsString(editEventDto);

        mockMvc.perform(put("/event/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editEventDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    void getAllEvents_NotLogged() throws Exception {
        mockMvc.perform(get("/event/all/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void getAllEvents_Student() throws Exception {
        mockMvc.perform(get("/event/all/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void getAllEvents_Teacher() throws Exception {
        mockMvc.perform(get("/event/all/61"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testadmin1@gmail.com", roles = {"ADMINISTRATOR"})
    void getAllEvents_Administrator() throws Exception {
        mockMvc.perform(get("/event/all/71"))
                .andExpect(status().isOk());
    }

    @Test
    void getMonthEvents_NotLogged() throws Exception {
        mockMvc.perform(get("/event/month/10/2024/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void getMonthEvents_Student() throws Exception {
        mockMvc.perform(get("/event/month/10/2024/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void getMonthEvents_Teacher() throws Exception {
        mockMvc.perform(get("/event/month/10/2024/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testadmin1@gmail.com", roles = {"ADMINISTRATOR"})
    void getMonthEvents_Administrator() throws Exception {
        mockMvc.perform(get("/event/month/10/2024/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllPastEvents_NotLogged() throws Exception {
        mockMvc.perform(get("/event/past"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void getAllPastEvents_Student() throws Exception {
        mockMvc.perform(get("/event/past"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void getAllPastEvents_Teacher() throws Exception {
        mockMvc.perform(get("/event/past"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testadmin1@gmail.com", roles = {"ADMINISTRATOR"})
    void getAllPastEvents_Administrator() throws Exception {
        mockMvc.perform(get("/event/past"))
                .andExpect(status().isOk());
    }

    @Test
    void getNextEvent_NotLogged() throws Exception {
        mockMvc.perform(get("/event/next/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void getNextEvent_Student() throws Exception {
        mockMvc.perform(get("/event/next/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void getNextEvent_Teacher() throws Exception {
        mockMvc.perform(get("/event/next/61"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testadmin1@gmail.com", roles = {"ADMINISTRATOR"})
    void getNextEvent_Administrator() throws Exception {
        mockMvc.perform(get("/event/next/71"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteEvent_NotLogged() throws Exception {
        mockMvc.perform(delete("/event/101"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void deleteEvent_Student() throws Exception {
        mockMvc.perform(delete("/event/101"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void deleteEvent_Teacher() throws Exception {
        mockMvc.perform(delete("/event/101"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testadmin1@gmail.com", roles = {"ADMINISTRATOR"})
    void deleteEvent_Administrator() throws Exception {
        mockMvc.perform(delete("/event/101"))
                .andExpect(status().isOk());
    }

    @Test
    void generatePdf_NotLogged() throws Exception {
        mockMvc.perform(get("/event/generatepdf/101"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void generatePdf_Student() throws Exception {
        mockMvc.perform(get("/event/generatepdf/101"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void generatePdf_Teacher() throws Exception {
        mockMvc.perform(get("/event/generatepdf/101"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testadmin1@gmail.com", roles = {"ADMINISTRATOR"})
    void generatePdf_Administrator() throws Exception {
        mockMvc.perform(get("/event/generatepdf/101"))
                .andExpect(status().isOk());
    }
}