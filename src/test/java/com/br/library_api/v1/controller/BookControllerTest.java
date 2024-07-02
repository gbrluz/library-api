package com.br.library_api.v1.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.br.library_api.domain.model.dto.BookDTO;
import com.br.library_api.v1.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void testCreateBook() throws Exception {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setName("Test Book");
        bookDTO.setIsbn("1234567890");
        bookDTO.setPublicationDate(LocalDate.of(2021, 1, 1));
        bookDTO.setAuthorIds(Arrays.asList(1L, 2L));

        BookDTO savedBookDTO = new BookDTO();
        savedBookDTO.setId(1L);
        savedBookDTO.setName("Test Book");
        savedBookDTO.setIsbn("1234567890");
        savedBookDTO.setPublicationDate(LocalDate.of(2021, 1, 1));
        savedBookDTO.setAuthorIds(Arrays.asList(1L, 2L));

        when(bookService.saveBook(any(BookDTO.class))).thenReturn(savedBookDTO);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Book"))
                .andExpect(jsonPath("$.isbn").value("1234567890"))
                .andExpect(jsonPath("$.publicationDate").value("2021-01-01"))
                .andExpect(jsonPath("$.authorIds[0]").value(1L))
                .andExpect(jsonPath("$.authorIds[1]").value(2L));
    }

    @Test
    void testGetAllBooks() throws Exception {
        BookDTO bookDTO1 = new BookDTO();
        bookDTO1.setId(1L);
        bookDTO1.setName("Book 1");
        bookDTO1.setIsbn("1234567890");
        bookDTO1.setPublicationDate(LocalDate.of(2021, 1, 1));
        bookDTO1.setAuthorIds(Arrays.asList(1L, 2L));

        BookDTO bookDTO2 = new BookDTO();
        bookDTO2.setId(2L);
        bookDTO2.setName("Book 2");
        bookDTO2.setIsbn("0987654321");
        bookDTO2.setPublicationDate(LocalDate.of(2022, 2, 2));
        bookDTO2.setAuthorIds(Arrays.asList(2L, 3L));

        List<BookDTO> books = Arrays.asList(bookDTO1, bookDTO2);

        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Book 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Book 2"));
    }

    @Test
    void testGetBookById() throws Exception {
        Long bookId = 1L;
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(bookId);
        bookDTO.setName("Test Book");
        bookDTO.setIsbn("1234567890");
        bookDTO.setPublicationDate(LocalDate.of(2021, 1, 1));
        bookDTO.setAuthorIds(Arrays.asList(1L, 2L));

        when(bookService.getBookById(bookId)).thenReturn(bookDTO);

        mockMvc.perform(get("/api/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.name").value("Test Book"))
                .andExpect(jsonPath("$.isbn").value("1234567890"))
                .andExpect(jsonPath("$.publicationDate").value("2021-01-01"))
                .andExpect(jsonPath("$.authorIds[0]").value(1L))
                .andExpect(jsonPath("$.authorIds[1]").value(2L));
    }

    @Test
    void testGetBookByIdWhenBookNotFound() throws Exception {
        Long bookId = 1L;

        when(bookService.getBookById(bookId)).thenReturn(null);

        mockMvc.perform(get("/api/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteBook() throws Exception {
        Long bookId = 1L;

        doNothing().when(bookService).deleteBook(bookId);

        mockMvc.perform(delete("/api/books/delete/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAvailableBooks() throws Exception {
        BookDTO bookDTO1 = new BookDTO();
        bookDTO1.setId(1L);
        bookDTO1.setName("Available Book 1");
        bookDTO1.setIsbn("1234567890");
        bookDTO1.setPublicationDate(LocalDate.of(2021, 1, 1));
        bookDTO1.setAuthorIds(Arrays.asList(1L, 2L));

        BookDTO bookDTO2 = new BookDTO();
        bookDTO2.setId(2L);
        bookDTO2.setName("Available Book 2");
        bookDTO2.setIsbn("0987654321");
        bookDTO2.setPublicationDate(LocalDate.of(2022, 2, 2));
        bookDTO2.setAuthorIds(Arrays.asList(2L, 3L));

        List<BookDTO> availableBooks = Arrays.asList(bookDTO1, bookDTO2);

        when(bookService.getAvailableBooks()).thenReturn(availableBooks);

        mockMvc.perform(get("/api/books/available")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Available Book 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Available Book 2"));
    }

    @Test
    void testGetRentedBooks() throws Exception {
        BookDTO bookDTO1 = new BookDTO();
        bookDTO1.setId(1L);
        bookDTO1.setName("Rented Book 1");
        bookDTO1.setIsbn("1234567890");
        bookDTO1.setPublicationDate(LocalDate.of(2021, 1, 1));
        bookDTO1.setAuthorIds(Arrays.asList(1L, 2L));

        BookDTO bookDTO2 = new BookDTO();
        bookDTO2.setId(2L);
        bookDTO2.setName("Rented Book 2");
        bookDTO2.setIsbn("0987654321");
        bookDTO2.setPublicationDate(LocalDate.of(2022, 2, 2));
        bookDTO2.setAuthorIds(Arrays.asList(2L, 3L));

        List<BookDTO> rentedBooks = Arrays.asList(bookDTO1, bookDTO2);

        when(bookService.getRentedBooks()).thenReturn(rentedBooks);

        mockMvc.perform(get("/api/books/rented")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Rented Book 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Rented Book 2"));
    }

    @Test
    void testGetBooksByAuthor() throws Exception {
        Long authorId = 1L;

        BookDTO bookDTO1 = new BookDTO();
        bookDTO1.setId(1L);
        bookDTO1.setName("Book 1");
        bookDTO1.setIsbn("1234567890");
        bookDTO1.setPublicationDate(LocalDate.of(2021, 1, 1));
        bookDTO1.setAuthorIds(Arrays.asList(authorId));

        BookDTO bookDTO2 = new BookDTO();
        bookDTO2.setId(2L);
        bookDTO2.setName("Book 2");
        bookDTO2.setIsbn("0987654321");
        bookDTO2.setPublicationDate(LocalDate.of(2022, 2, 2));
        bookDTO2.setAuthorIds(Arrays.asList(authorId));

        List<BookDTO> books = Arrays.asList(bookDTO1, bookDTO2);

        when(bookService.getBooksByAuthor(authorId)).thenReturn(books);

        mockMvc.perform(get("/api/books/author/{authorId}", authorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Book 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Book 2"));
    }

    @Test
    void testDeleteBookIfNotRented() throws Exception {
        Long bookId = 1L;

        doNothing().when(bookService).deleteBookIfNotRented(bookId);

        mockMvc.perform(delete("/api/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteBookIfNotRentedWhenBookIsRented() throws Exception {
        Long bookId = 1L;

        doThrow(new RuntimeException("Book cannot be deleted as it has been rented"))
                .when(bookService).deleteBookIfNotRented(bookId);

        mockMvc.perform(delete("/api/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }
}