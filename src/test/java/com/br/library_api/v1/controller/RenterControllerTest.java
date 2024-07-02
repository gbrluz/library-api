package com.br.library_api.v1.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.br.library_api.domain.model.dto.RenterDTO;
import com.br.library_api.v1.service.RenterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

public class RenterControllerTest {
    @Mock
    private RenterService renterService;

    @InjectMocks
    private RenterController renterController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(renterController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateRenter() throws Exception {
        RenterDTO renterDTO = new RenterDTO();
        renterDTO.setName("Test Renter");
        renterDTO.setEmail("test@example.com");

        RenterDTO savedRenterDTO = new RenterDTO();
        savedRenterDTO.setId(1L);
        savedRenterDTO.setName("Test Renter");
        savedRenterDTO.setEmail("test@example.com");

        when(renterService.saveRenter(any(RenterDTO.class))).thenReturn(savedRenterDTO);

        mockMvc.perform(post("/api/renters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(renterDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Renter"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testGetAllRenters() throws Exception {
        RenterDTO renterDTO1 = new RenterDTO();
        renterDTO1.setId(1L);
        renterDTO1.setName("Renter 1");
        renterDTO1.setEmail("renter1@example.com");

        RenterDTO renterDTO2 = new RenterDTO();
        renterDTO2.setId(2L);
        renterDTO2.setName("Renter 2");
        renterDTO2.setEmail("renter2@example.com");

        List<RenterDTO> renters = Arrays.asList(renterDTO1, renterDTO2);

        when(renterService.getAllRenters()).thenReturn(renters);

        mockMvc.perform(get("/api/renters")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Renter 1"))
                .andExpect(jsonPath("$[0].email").value("renter1@example.com"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Renter 2"))
                .andExpect(jsonPath("$[1].email").value("renter2@example.com"));
    }

    @Test
    void testGetRenterById() throws Exception {
        Long renterId = 1L;
        RenterDTO renterDTO = new RenterDTO();
        renterDTO.setId(renterId);
        renterDTO.setName("Test Renter");
        renterDTO.setEmail("test@example.com");

        when(renterService.getRenterById(renterId)).thenReturn(renterDTO);

        mockMvc.perform(get("/api/renters/{id}", renterId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(renterId))
                .andExpect(jsonPath("$.name").value("Test Renter"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testGetRenterByIdWhenRenterNotFound() throws Exception {
        Long renterId = 1L;

        when(renterService.getRenterById(renterId)).thenReturn(null);

        mockMvc.perform(get("/api/renters/{id}", renterId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteRenter() throws Exception {
        Long renterId = 1L;

        doNothing().when(renterService).deleteRenter(renterId);

        mockMvc.perform(delete("/api/renters/{id}", renterId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteRenterWhenRenterHasARent() throws Exception {
        Long renterId = 1L;

        doThrow(new RuntimeException("Cannot delete")).when(renterService).deleteRenter(renterId);

        mockMvc.perform(delete("/api/renters/{id}", renterId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }
}
