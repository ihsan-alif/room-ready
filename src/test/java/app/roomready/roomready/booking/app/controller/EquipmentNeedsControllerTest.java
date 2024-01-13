package app.roomready.roomready.booking.app.controller;

import app.roomready.roomready.booking.app.dto.request.EquipmentRequest;
import app.roomready.roomready.booking.app.dto.request.SearchEquipmentRequest;
import app.roomready.roomready.booking.app.dto.response.EquipmentNeedsResponse;
import app.roomready.roomready.booking.app.dto.response.WebResponse;
import app.roomready.roomready.booking.app.repository.EquipmentNeedsRepository;
import app.roomready.roomready.booking.app.service.EquipmentNeedsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "initadmin",roles = "ADMIN")
class EquipmentNeedsControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EquipmentNeedsService equipmentNeedsService;

    @Test
    void create() throws Exception {
        EquipmentRequest request = new EquipmentRequest();
        request.setId(UUID.randomUUID().toString());
        request.setName("baju wibu");
        request.setStock(100L);
        EquipmentNeedsResponse equipmentNeedsResponse = new EquipmentNeedsResponse();
        equipmentNeedsResponse.setId(request.getId());
        equipmentNeedsResponse.setName(request.getName());
        equipmentNeedsResponse.setStock(request.getStock());

        when(equipmentNeedsService.create(any(EquipmentRequest.class))).thenReturn(equipmentNeedsResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/equipment-needs/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.message").value("successfully create new equipment needs"));


    }

    @Test
    void getById() throws Exception {
        String id = "b7fcf54e-541d-44fd-9d79-fdfbc1d2977d";
        EquipmentNeedsResponse equipmentNeedsResponse = new EquipmentNeedsResponse();
        equipmentNeedsResponse.setId("b7fcf54e-541d-44fd-9d79-fdfbc1d2977d");
        equipmentNeedsResponse.setName("kacamata");
        equipmentNeedsResponse.setStock(86L);

        when(equipmentNeedsService.getById(any(String.class))).thenReturn(equipmentNeedsResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/equipment-needs/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.message").value("successfully get by id equipment needs"));
    }


    @Test
    void delete() throws Exception {
        String id = "990ea371-fb39-49bc-b1e1-8af7a298137f";

        doNothing().when(equipmentNeedsService).delete(any(String.class));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/equipment-needs/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.message").value("successfully delete equipment needs"))
                .andExpect(jsonPath("$.data").value("OK"));
    }

    @Test
    void search() throws Exception {
        String name = "kacamata";
        Long stock = 100L;
        Integer page = 1;
        Integer size = 10;

        List<EquipmentNeedsResponse> equipmentList = Collections.singletonList(new EquipmentNeedsResponse(UUID.randomUUID().toString(),name,stock));
        PageImpl<EquipmentNeedsResponse> pageResult = new PageImpl<>(equipmentList, PageRequest.of(page, size), 1L);

        when(equipmentNeedsService.search(any(SearchEquipmentRequest.class))).thenReturn(pageResult);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/equipment-needs/")
                        .param("name", name)
                        .param("stock", String.valueOf(stock))
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.message").value("successfully get equipment needs"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("kacamata"))
                .andExpect(jsonPath("$.paging.page").value(page))
                .andExpect(jsonPath("$.paging.size").value(size))
                .andExpect(jsonPath("$.paging.totalPage").value(2))
                .andExpect(jsonPath("$.paging.totalElements").value(11));
    }
}