package com.br.library_api.v1;

import com.br.library_api.domain.repository.RentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class RentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RentRepository rentRepository;

    @Test
    public void testRentWithLateFee() throws Exception {
        // Defina o JSON para o aluguel
        String rentJson = "{ \"renterId\": 1, \"bookIds\": [1], \"pickupDate\": \"2024-10-01\", \"returnDate\": \"2024-10-07\" }";

        // Simula uma requisição POST para registrar o aluguel
        mockMvc.perform(post("/api/rents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lateFee").value(3.0)); // Multa de atraso será 3, pois devolução está 3 dias atrasada

    }
}