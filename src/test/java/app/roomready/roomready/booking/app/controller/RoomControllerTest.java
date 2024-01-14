package app.roomready.roomready.booking.app.controller;

import app.roomready.roomready.booking.app.dto.request.RoomRequest;
import app.roomready.roomready.booking.app.dto.request.RoomUpdateRequest;
import app.roomready.roomready.booking.app.dto.request.SearchRoomRequest;
import app.roomready.roomready.booking.app.dto.request.UpdateRoomStatusRequest;
import app.roomready.roomready.booking.app.dto.response.EquipmentNeedsResponse;
import app.roomready.roomready.booking.app.dto.response.RoomResponse;
import app.roomready.roomready.booking.app.dto.response.WebResponse;
import app.roomready.roomready.booking.app.service.RoomService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(username = "initadmin", roles = "ADMIN")
public class RoomControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RoomService roomService;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateNewRoomSuccess() throws Exception {
        RoomRequest roomRequest = new RoomRequest();
        roomRequest.setName("new room 2");
        roomRequest.setCapacities(5);
        roomRequest.setStatus("available");
        roomRequest.setFacilities("WiFi");

        doReturn(new RoomResponse()).when(roomService).createNew(any(RoomRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("{}"))
                .andReturn();
    }

    @Test
    public void testCreateNewRoomConflict() throws Exception {

        RoomRequest roomRequest = new RoomRequest();
        roomRequest.setName("stringray");

        doThrow(new ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT, "room name already exist"))
                .when(roomService).createNew(any(RoomRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomRequest)))
                        .andExpect(MockMvcResultMatchers.status().isConflict())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("room name already exist"));
    }

    @Test
    public void testGetRoomByIdSuccess() throws Exception{
        String id = "c092ae7b-7e61-4a0a-9db2-c967b7c1a89e";
        RoomResponse roomResponse = new RoomResponse();
        roomResponse.setId(id);
        roomResponse.setName("stringray");
        roomResponse.setCapacities(1);
        roomResponse.setStatus("booked");
        roomResponse.setFacilities("string");

        when(roomService.getById(any(String.class))).thenReturn(roomResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("successfully get room by id"));
    }

    @Test
    public void testGetRoomByIdNotFound()throws Exception{
        String id = "";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testGetAllRoom()throws Exception{
        mockMvc.perform(
                        get("/api/rooms").accept(MediaType.APPLICATION_JSON)
                                .param("page","1")
                                .param("size","10")
                ).andExpect(status().isOk());
    }

    @Test
    public void testDeleteRoomByIdSuccess() throws Exception{
        String id = "c092ae7b-7e61-4a0a-9db2-c967b7c1a89e";

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/rooms/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("successfully delete room"));
    }

    @Test
    public void testDeleteRoomByIdNotFound()throws Exception{
        String id = "";

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/rooms/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testUpdateRoomSuccess() throws Exception{
        String id = "c092ae7b-7e61-4a0a-9db2-c967b7c1a89e";

        RoomUpdateRequest request = new RoomUpdateRequest();
        request.setId(id);
        request.setName("stringray");
        request.setCapacities(1);
        request.setStatus("available");
        request.setFacilities("string");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/rooms/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("successfully update room"));
    }

    @Test
    public void testUpdateRoomIdNotFound()throws Exception{
        String id = "";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/rooms/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testUpdateStatusRoom()throws Exception{
        String id = "efa495a2-d42a-4c9b-8de5-8d7dbba8026d";

        UpdateRoomStatusRequest request = new UpdateRoomStatusRequest();
        request.setId(id);
        request.setStatus("available");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/rooms/{id}/status", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("successfully change status room"));
    }

    @Test
    public void testUpdateStatusRoomIdNotFound()throws Exception{
        String id = "";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/rooms/{id}/status", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
