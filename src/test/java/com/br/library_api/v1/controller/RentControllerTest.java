package com.br.library_api.v1.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.br.library_api.domain.model.dto.BookDTO;
import com.br.library_api.domain.model.dto.RentDTO;
import com.br.library_api.v1.service.RentService;
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

public class RentControllerTest {
    @Mock
    private RentService rentService;

    @InjectMocks
    private RentController rentController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(rentController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateRent() throws Exception {
        RentDTO rentDTO = new RentDTO();
        rentDTO.setRenterId(1L);

        RentDTO savedRentDTO = new RentDTO();
        savedRentDTO.setId(1L);
        savedRentDTO.setRenterId(1L);

        when(rentService.saveRent(any(RentDTO.class))).thenReturn(savedRentDTO);

        mockMvc.perform(post("/api/rents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.renterId").value(1L));
    }

    @Test
    void testGetAllRents() throws Exception {
        RentDTO rentDTO1 = new RentDTO();
        rentDTO1.setId(1L);
        rentDTO1.setRenterId(1L);

        RentDTO rentDTO2 = new RentDTO();
        rentDTO2.setId(2L);
        rentDTO2.setRenterId(2L);

        List<RentDTO> rents = Arrays.asList(rentDTO1, rentDTO2);

        when(rentService.getAllRents()).thenReturn(rents);

        mockMvc.perform(get("/api/rents")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].renterId").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].renterId").value(2L));
    }

    @Test
    void testGetRentById() throws Exception {
        Long rentId = 1L;
        RentDTO rentDTO = new RentDTO();
        rentDTO.setId(rentId);
        rentDTO.setRenterId(1L);

        when(rentService.getRentById(rentId)).thenReturn(rentDTO);

        mockMvc.perform(get("/api/rents/{id}", rentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(rentId))
                .andExpect(jsonPath("$.renterId").value(1L));
    }

    @Test
    void testGetRentByIdWhenRentNotFound() throws Exception {
        Long rentId = 1L;

        when(rentService.getRentById(rentId)).thenReturn(null);

        mockMvc.perform(get("/api/rents/{id}", rentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetBooksRentedByRenter() throws Exception {
        Long renterId = 1L;
        BookDTO bookDTO1 = new BookDTO();
        bookDTO1.setId(1L);
        bookDTO1.setName("Book 1");

        BookDTO bookDTO2 = new BookDTO();
        bookDTO2.setId(2L);
        bookDTO2.setName("Book 2");

        List<BookDTO> rentedBooks = Arrays.asList(bookDTO1, bookDTO2);

        when(rentService.getBooksRentedByRenter(renterId)).thenReturn(rentedBooks);

        mockMvc.perform(get("/api/rents/renter/{renterId}/books", renterId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Book 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Book 2"));
    }

    @Test
    void testDeleteRent() throws Exception {
        Long rentId = 1L;

        doNothing().when(rentService).deleteRent(rentId);

        mockMvc.perform(delete("/api/rents/{id}", rentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
