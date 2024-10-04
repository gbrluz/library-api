package com.br.library_api.v1.service;

import com.br.library_api.client.GoogleBookItem;
import com.br.library_api.client.GoogleBookVolumeInfo;
import com.br.library_api.client.GoogleBooksClient;
import com.br.library_api.client.GoogleBooksResponse;
import com.br.library_api.domain.model.dto.BookDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookServiceIntegrationTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private GoogleBooksClient googleBooksClient; // Simula a API do Google Books

    @Test
    public void testValidateIsbnWithGoogleBooks() {
        // Simular resposta da Google Books API
        GoogleBooksResponse mockResponse = new GoogleBooksResponse();
        GoogleBookItem mockItem = new GoogleBookItem();
        GoogleBookVolumeInfo volumeInfo = new GoogleBookVolumeInfo();
        volumeInfo.setTitle("Clean Code");
        mockItem.setVolumeInfo(volumeInfo);
        mockResponse.setItems(Collections.singletonList(mockItem));

        // Quando a API Google Books for chamada, retorne o mockResponse
        when(googleBooksClient.getBookByIsbn("isbn:9780132350884")).thenReturn(mockResponse);

        // Criação de um DTO de livro para o teste
        BookDTO bookDTO = new BookDTO();
        bookDTO.setName("Test Book");
        bookDTO.setIsbn("9780132350884");
        bookDTO.setPublicationDate(LocalDate.of(2008,8,1));
        bookDTO.setAuthorIds(new ArrayList<>());

        // Salvar livro e verificar se a validação do ISBN foi bem-sucedida
        BookDTO savedBook = bookService.saveBook(bookDTO);

        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getName());
        verify(googleBooksClient, times(1)).getBookByIsbn("isbn:9780132350884");
    }
}

