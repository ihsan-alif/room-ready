package app.roomready.roomready.booking.app.controller;

import app.roomready.roomready.booking.app.dto.request.LoginRequest;
import app.roomready.roomready.booking.app.dto.response.LoginResponse;
import app.roomready.roomready.booking.app.dto.response.RegisterResponse;
import app.roomready.roomready.booking.app.dto.response.WebResponse;
import app.roomready.roomready.booking.app.repository.UserCredentialRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerLoginTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void loginSuccess() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("initadmin");
        request.setPassword("password");

        mockMvc.perform(
                post("http://localhost:8080/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<LoginResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            LoginResponse data = response.getData();
            List<String> role = data.getRole();

            assertEquals("OK", response.getStatus());
            assertEquals("successfully login", response.getMessage());
            assertNotNull(data.getToken());
            assertTrue(role.contains("ROLE_ADMIN"));
        });
    }

    @Test
    void loginUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("unknown");
        request.setPassword("password");

        mockMvc.perform(
                post("http://localhost:8080/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<LoginResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertEquals("Unauthorized", response.getStatus());
            assertEquals("unauthorized", response.getMessage());
            assertNull(response.getData());
        });
    }
}
