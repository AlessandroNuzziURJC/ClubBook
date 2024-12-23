package clubbook.backend.controller;

import clubbook.backend.model.enumClasses.RoleEnum;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("/scripts/roles_dataset.sql")
public class AuthenticationControllerIntegrationTest {

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

    @Transactional
    @Test
    @WithMockUser(username = "administrator", roles = {"ADMINISTRATOR"})
    public void testCreateUserCorrect() throws Exception {
        String userJson = "{"
                + "\"email\": \"student@prueba.com\","
                + "\"password\": \"password\","
                + "\"role\": \"" + RoleEnum.STUDENT.name() + "\","
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
                        .content(userJson))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    public void testCreateUserIncorrect() throws Exception {
        String userJson = "{"
                + "\"password\": \"password\","
                + "\"role\": \"" + RoleEnum.STUDENT.name() + "\","
                + "\"firstName\": \"Marty\","
                + "\"lastName\": \"McFly\","
                + "\"phoneNumber\": \"767676767\","
                + "\"birthday\": \"1968-06-09\""
                + "}";

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Transactional
    @Test
    @WithMockUser(username = "administrator", roles = {"ADMINISTRATOR"})
    public void testLoginUserCorrect() throws Exception {
        String userJson = "{"
                + "\"email\": \"student@prueba.com\","
                + "\"password\": \"password\","
                + "\"role\": \"" + RoleEnum.STUDENT.name() + "\","
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
                        .content(userJson))
                .andExpect(status().isOk());

        String userLoginJson = "{"
                + "\"email\": \"student@prueba.com\","
                + "\"password\": \"password\""
                + "}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userLoginJson))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    @WithMockUser(username = "administrator", roles = {"ADMINISTRATOR"})
    public void testLoginUserIncorrect() throws Exception {
        String userJson = "{"
                + "\"email\": \"student@prueba.com\","
                + "\"password\": \"password\","
                + "\"role\": \"" + RoleEnum.STUDENT.name() + "\","
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
                        .content(userJson))
                .andExpect(status().isOk());

        String userLoginJson = "{"
                + "\"email\": \"student@prueba.com\","
                + "\"password\": \"wrongpassword\""
                + "}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userLoginJson))
                .andExpect(status().isUnauthorized())
                .andExpect(result -> System.out.println(result.getResponse().getContentAsString()));
    }

    @Transactional
    @Test
    @WithMockUser(username = "administrator", roles = {"ADMINISTRATOR"})
    public void testLogoutUserCorrect() throws Exception {
        String userJson = "{"
                + "\"email\": \"student@prueba.com\","
                + "\"password\": \"password\","
                + "\"role\": \"" + RoleEnum.STUDENT.name() + "\","
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
                        .content(userJson))
                .andExpect(status().isOk());

        String userLoginJson = "{"
                + "\"email\": \"student@prueba.com\","
                + "\"password\": \"password\""
                + "}";

        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userLoginJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String token = extractValue(response, "token");

        mockMvc.perform(get("/auth/logout")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    @WithMockUser(username = "administrator", roles = {"ADMINISTRATOR"})
    public void testLoginDeletedUserCorrect() throws Exception {
        String userJson = "{"
                + "\"email\": \"student@prueba.com\","
                + "\"password\": \"password\","
                + "\"role\": \"" + RoleEnum.STUDENT.name() + "\","
                + "\"firstName\": \"Marty\","
                + "\"lastName\": \"McFly\","
                + "\"phoneNumber\": \"767676767\","
                + "\"birthday\": \"1968-06-09\","
                + "\"address\": \"Calla del Olvido, 1\","
                + "\"idCard\": \"123123123X\","
                + "\"partner\": \"true\""
                + "}";

        ResultActions resultActions = mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk());

        JSONObject jsonObject = new JSONObject(resultActions.andReturn().getResponse().getContentAsString());
        int id = jsonObject.getJSONObject("data").getInt("id");

        mockMvc.perform(delete("/user/" + id)).andExpect(status().isOk());;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().is4xxClientError());
    }
}
