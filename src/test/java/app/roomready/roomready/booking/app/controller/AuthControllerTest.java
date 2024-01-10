package app.roomready.roomready.booking.app.controller;

import app.roomready.roomready.booking.app.constant.ERole;
import app.roomready.roomready.booking.app.dto.request.UserRegisterRequest;
import app.roomready.roomready.booking.app.dto.response.RegisterResonse;
import app.roomready.roomready.booking.app.dto.response.WebResponse;
import app.roomready.roomready.booking.app.entity.Role;
import app.roomready.roomready.booking.app.entity.UserCredential;
import app.roomready.roomready.booking.app.repository.UserCredentialRepository;
import app.roomready.roomready.booking.app.service.RoleService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        userCredentialRepository.deleteAll();
    }

    @Test
    void testRegisterUserSuccess() throws Exception {
        UserCredential user = new UserCredential();
        user.setUsername("initadmin");
        user.setPassword("password");
        Role role = roleService.getOrSave(ERole.ROLE_ADMIN);
        user.setRoles(List.of(role));

        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername("testusername");
        request.setPassword("secretpassword");

        mockMvc.perform(
                post("/api/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            WebResponse<RegisterResonse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    }
            );

            assertEquals("CREATED", response.getStatus());
            assertEquals("successfully create new user", response.getMessage());
            assertNotNull(response.getData());
        });
    }
}