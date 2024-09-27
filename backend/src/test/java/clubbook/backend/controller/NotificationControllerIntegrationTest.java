package clubbook.backend.controller;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"/scripts/roles_dataset.sql", "/scripts/dataset.sql", "/scripts/notification.sql"})
class NotificationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Transactional
    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void existTokenAuthenticated() throws Exception {
        mockMvc.perform(get("/notification/token/1?notificationToken=token_1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void notExistTokenAuthenticated() throws Exception {
        mockMvc.perform(get("/notification/token/1?notificationToken=token_6")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Transactional
    @Test
    void existTokenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/notification/token/1?notificationToken=token_1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    void notExistTokenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/notification/token/1?notificationToken=token_6")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void postTokenAuthenticated() throws Exception {
        mockMvc.perform(post("/notification/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"token\":\"token_10\", \"id\": 21}"))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    void postTokenNotAuthenticated() throws Exception {
        mockMvc.perform(post("/notification/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":\"token_10\", \"id\": 21}"))
                .andExpect(status().isForbidden());
    }

    @Transactional
    @Test
    @WithMockUser(username = "teststudent1@gmail.com", roles = {"STUDENT"})
    void getNotificationsAuthenticated() throws Exception {
        mockMvc.perform(get("/notification/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    void getNotificationsNotAuthenticated() throws Exception {
        mockMvc.perform(get("/notification/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}