package app.roomready.roomready.booking.app.controller;

import app.roomready.roomready.booking.app.constant.ETrans;
import app.roomready.roomready.booking.app.dto.request.ApprovalRequestReservation;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
@WithMockUser(username = "initadmin",roles = "ADMIN")
class ApprovalControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    @WithMockUser(username = "initadmin",roles = "ADMIN")
    void getAll() throws Exception {

        mockMvc.perform(
                get("/api/approval").accept(MediaType.APPLICATION_JSON)
                        .param("page","1")
                        .param("size","10")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Success Get All Data"))
                .andExpect(jsonPath("$.paging.page").value(1))
                .andExpect(jsonPath("$.paging.size").value(1));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "GA"})
    void getApprovalById() throws Exception {
        String id = "363755a7-9b6b-4649-9136-f84c311912ca";

        // Perform MockMvc request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/approval/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

    }

    @Test
    @WithMockUser(roles = {"ADMIN", "GA"})
    public void testGetApprovalByIdEmptyId() throws Exception {
        String id = "";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/approval/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    void updateApproval() throws Exception {

        ApprovalRequestReservation request = new ApprovalRequestReservation();
        request.setReservationId("7a35151f-acab-4ddb-bae9-a101cbd5172c");
        request.setApprovedBy("ujang");
        request.setApprovedStatus(ETrans.DECLINE);
        request.setRejection("Not ACCEPTANCE");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/approval")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Succeed"));

    }



    @Test
    void deleteApproval() throws Exception {
        String id = "d7b02132-372d-4d54-9488-f1cf79feda7d";

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/approval/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Succes Delete Data By id"));
    }

    @Test
    public void testGetFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/approval/download"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=approval.csv"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.parseMediaType("application/csv")));
    }
}