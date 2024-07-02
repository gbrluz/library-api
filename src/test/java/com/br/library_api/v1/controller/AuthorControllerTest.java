package com.br.library_api.v1.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.br.library_api.domain.model.dto.AuthorDTO;
import com.br.library_api.v1.service.AuthorService;
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

public class AuthorControllerTest {
    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authorController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateAuthor() throws Exception {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setName("Test Author");
        authorDTO.setCpf("12345678901");
        authorDTO.setBirthYear(1980);

        AuthorDTO savedAuthorDTO = new AuthorDTO();
        savedAuthorDTO.setId(1L);
        savedAuthorDTO.setName("Test Author");
        savedAuthorDTO.setCpf("12345678901");
        savedAuthorDTO.setBirthYear(1980);

        when(authorService.saveAuthor(any(AuthorDTO.class))).thenReturn(savedAuthorDTO);

        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Author"))
                .andExpect(jsonPath("$.cpf").value("12345678901"))
                .andExpect(jsonPath("$.birthYear").value(1980));
    }

    @Test
    void testGetAllAuthors() throws Exception {
        AuthorDTO authorDTO1 = new AuthorDTO();
        authorDTO1.setId(1L);
        authorDTO1.setName("Author 1");
        authorDTO1.setCpf("12345678901");
        authorDTO1.setBirthYear(1980);

        AuthorDTO authorDTO2 = new AuthorDTO();
        authorDTO2.setId(2L);
        authorDTO2.setName("Author 2");
        authorDTO2.setCpf("09876543210");
        authorDTO2.setBirthYear(1990);

        List<AuthorDTO> authors = Arrays.asList(authorDTO1, authorDTO2);

        when(authorService.getAllAuthors()).thenReturn(authors);

        mockMvc.perform(get("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Author 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Author 2"));
    }

    @Test
    void testGetAuthorById() throws Exception {
        Long authorId = 1L;
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(authorId);
        authorDTO.setName("Test Author");
        authorDTO.setCpf("12345678901");
        authorDTO.setBirthYear(1980);

        when(authorService.getAuthorById(authorId)).thenReturn(authorDTO);

        mockMvc.perform(get("/api/authors/{id}", authorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(authorId))
                .andExpect(jsonPath("$.name").value("Test Author"))
                .andExpect(jsonPath("$.cpf").value("12345678901"))
                .andExpect(jsonPath("$.birthYear").value(1980));
    }

    @Test
    void testGetAuthorByIdWhenAuthorNotFound() throws Exception {
        Long authorId = 1L;

        when(authorService.getAuthorById(authorId)).thenReturn(null);

        mockMvc.perform(get("/api/authors/{id}", authorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAuthorsByName() throws Exception {
        String name = "Author";

        AuthorDTO authorDTO1 = new AuthorDTO();
        authorDTO1.setId(1L);
        authorDTO1.setName("Author 1");
        authorDTO1.setCpf("12345678901");
        authorDTO1.setBirthYear(1980);

        AuthorDTO authorDTO2 = new AuthorDTO();
        authorDTO2.setId(2L);
        authorDTO2.setName("Author 2");
        authorDTO2.setCpf("09876543210");
        authorDTO2.setBirthYear(1990);

        List<AuthorDTO> authors = Arrays.asList(authorDTO1, authorDTO2);

        when(authorService.getAuthorsByName(name)).thenReturn(authors);

        mockMvc.perform(get("/api/authors/byName")
                        .param("name", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Author 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Author 2"));
    }

    @Test
    void testDeleteAuthorWhenSuccess() throws Exception {
        Long authorId = 1L;

        doNothing().when(authorService).deleteAuthorById(authorId);

        mockMvc.perform(delete("/api/authors/{id}", authorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteAuthorWhenAuthorHasAssociatedBooks() throws Exception {
        Long authorId = 1L;

        doThrow(new RuntimeException("Author cannot be deleted as it has associated books"))
                .when(authorService).deleteAuthorById(authorId);

        mockMvc.perform(delete("/api/authors/{id}", authorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }
}
