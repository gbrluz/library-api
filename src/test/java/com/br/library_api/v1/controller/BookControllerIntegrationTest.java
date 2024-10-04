package com.br.library_api.v1.controller;

import com.br.library_api.domain.model.Book;
import com.br.library_api.domain.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll(); // Limpa o banco de dados após cada teste
    }

    @Test
    public void testCreateBook() throws Exception {
        // Define o JSON para o livro a ser criado
        String bookJson = "{ \"name\": \"Clean Code\", \"isbn\": \"9780132350884\", \"publicationDate\": \"2008-08-01\", \"authorIds\": [] }";

        // Simula uma requisição POST para cadastrar um livro e verifica o resultado
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isOk()) // Verifica se o status é 200 OK
                .andExpect(jsonPath("$.name").value("Clean Code")) // Verifica o nome do livro
                .andExpect(jsonPath("$.isbn").value("9780132350884")); // Verifica o ISBN

        // Verifica se o livro foi salvo no banco de dados
        List<Book> books = bookRepository.findAll();
        assertEquals("Clean Code", books.get(0).getName());
        assertEquals("9780132350884", books.get(0).getIsbn());
    }
}

