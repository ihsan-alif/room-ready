package app.roomready.roomready.booking.app.controller;

import app.roomready.roomready.booking.app.constant.ERole;
import app.roomready.roomready.booking.app.dto.request.UserRegisterRequest;
import app.roomready.roomready.booking.app.dto.response.RegisterResponse;
import app.roomready.roomready.booking.app.dto.response.WebResponse;
import app.roomready.roomready.booking.app.entity.Role;
import app.roomready.roomready.booking.app.entity.UserCredential;
import app.roomready.roomready.booking.app.repository.UserCredentialRepository;
import app.roomready.roomready.booking.app.service.RoleService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerRegisterTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Autowired
    private ObjectMapper objectMapper;

    String token;

    @Autowired
    private RoleService roleService;

    @BeforeEach
    void setUp() throws Exception {
        String requestBody = "{\"username\": \"initadmin\", \"password\": \"password\"}";

        ResultActions resultActions = mockMvc.perform(
                post("http://localhost:8080/api/auth/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        JSONObject object = new JSONObject(content);
        token = "Bearer " + object.getJSONObject("data").getString("token");
    }

    @Test
    void testRegisterUserSuccess() throws Exception {

        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername("testusername");
        request.setPassword("secretpassword");

        mockMvc.perform(
                post("/api/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            WebResponse<RegisterResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            RegisterResponse data = response.getData();
            List<String> role = data.getRole();

            assertEquals("Created", response.getStatus());
            assertEquals("successfully create new user", response.getMessage());
            assertEquals("testusername", data.getUsername());
            assertTrue(role.contains("ROLE_EMPLOYEE"));
        });
    }

    @Test
    void testRegisterUserBadRequest() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername("");
        request.setPassword("secret");

        mockMvc.perform(
                post("/api/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<RegisterResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertEquals("Bad Request", response.getStatus());
            assertNotNull(response.getMessage());
            assertNull(response.getData());
        });
    }

    @Test
    void testRegisterUserConflict() throws Exception {
        UserCredential user = userCredentialRepository.findByUsername("newusername")
                .orElseThrow();
        userCredentialRepository.delete(user);

        Role role = roleService.getOrSave(ERole.ROLE_EMPLOYEE);
        UserCredential userCredential = new UserCredential();
        userCredential.setUsername("newusername");
        userCredential.setPassword("secretpassword");
        userCredential.setRoles(List.of(role));
        userCredentialRepository.save(userCredential);

        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername("newusername");
        request.setPassword("secretpassword");

        mockMvc.perform(
                post("/api/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isConflict()
        ).andDo(result -> {
            WebResponse<RegisterResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertEquals("Conflict", response.getStatus());
            assertEquals("Data duplicate", response.getMessage());
            assertNull(response.getData());
        });
    }
}