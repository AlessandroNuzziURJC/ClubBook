package clubbook.backend.controller;

import clubbook.backend.dtos.UpdateEventAttendanceDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"/scripts/roles_dataset.sql", "/scripts/dataset.sql", "/scripts/events.sql"})
class EventAttendanceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // getStudentAttendance Tests
    @Test
    void getStudentAttendance_NotLogged() throws Exception {
        mockMvc.perform(get("/event_attendance/102/students"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void getStudentAttendance_Teacher() throws Exception {
        mockMvc.perform(get("/event_attendance/102/students"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void getStudentAttendance_Administrator() throws Exception {
        mockMvc.perform(get("/event_attendance/102/students"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void getStudentAttendance_StudentForbidden() throws Exception {
        mockMvc.perform(get("/event_attendance/102/students"))
                .andExpect(status().isForbidden());
    }

    // getTeacherAttendance Tests
    @Test
    void getTeacherAttendance_NotLogged() throws Exception {
        mockMvc.perform(get("/event_attendance/102/teachers"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void getTeacherAttendance_Teacher() throws Exception {
        mockMvc.perform(get("/event_attendance/102/teachers"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void getTeacherAttendance_Administrator() throws Exception {
        mockMvc.perform(get("/event_attendance/102/teachers"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void getTeacherAttendance_StudentForbidden() throws Exception {
        mockMvc.perform(get("/event_attendance/102/teachers"))
                .andExpect(status().isForbidden());
    }

    // getUserAttendance Tests
    @Test
    void getUserAttendance_NotLogged() throws Exception {
        mockMvc.perform(get("/event_attendance/102/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void getUserAttendance_Teacher() throws Exception {
        mockMvc.perform(get("/event_attendance/102/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void getUserAttendance_Student() throws Exception {
        mockMvc.perform(get("/event_attendance/102/1"))
                .andExpect(status().isOk());
    }

    @Test
    void updateAttendance_NotLogged() throws Exception {
        UpdateEventAttendanceDto updateEventAttendanceDto = new UpdateEventAttendanceDto();
        updateEventAttendanceDto.setEventId(102);
        updateEventAttendanceDto.setUserId(1);
        updateEventAttendanceDto.setStatus(true);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonRequest = objectMapper.writeValueAsString(updateEventAttendanceDto);

        mockMvc.perform(put("/event_attendance/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testteacher1@gmail.com", roles = {"TEACHER"})
    void updateAttendance_Teacher() throws Exception {
        UpdateEventAttendanceDto updateEventAttendanceDto = new UpdateEventAttendanceDto();
        updateEventAttendanceDto.setEventId(102);
        updateEventAttendanceDto.setUserId(61);
        updateEventAttendanceDto.setStatus(true);
        updateEventAttendanceDto.setEventAttendanceId(109);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonRequest = objectMapper.writeValueAsString(updateEventAttendanceDto);

        mockMvc.perform(put("/event_attendance/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void updateAttendance_Student() throws Exception {
        UpdateEventAttendanceDto updateEventAttendanceDto = new UpdateEventAttendanceDto();
        updateEventAttendanceDto.setEventId(102);
        updateEventAttendanceDto.setUserId(1);
        updateEventAttendanceDto.setStatus(true);
        updateEventAttendanceDto.setEventAttendanceId(101);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonRequest = objectMapper.writeValueAsString(updateEventAttendanceDto);

        mockMvc.perform(put("/event_attendance/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testadministrator1@gmail.com", roles = {"ADMINISTRATOR"})
    void updateAttendance_Administrator() throws Exception {
        UpdateEventAttendanceDto updateEventAttendanceDto = new UpdateEventAttendanceDto();
        updateEventAttendanceDto.setEventId(102);
        updateEventAttendanceDto.setUserId(71);
        updateEventAttendanceDto.setStatus(true);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonRequest = objectMapper.writeValueAsString(updateEventAttendanceDto);

        mockMvc.perform(put("/event_attendance/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());
    }
}