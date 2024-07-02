package com.br.library_api.v1.service;

import com.br.library_api.domain.model.Author;
import com.br.library_api.domain.model.Book;
import com.br.library_api.domain.model.dto.AuthorDTO;
import com.br.library_api.domain.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthorServiceTest {
    @InjectMocks
    private AuthorService authorService;

    @Mock
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveAuthor() {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setName("Test Author");
        authorDTO.setGender("Male");
        authorDTO.setBirthYear(1980);
        authorDTO.setCpf("12345678900");

        Author author = new Author();
        author.setId(1L);
        author.setName("Test Author");
        author.setGender("Male");
        author.setBirthYear(1980);
        author.setCpf("12345678900");

        when(authorRepository.save(any(Author.class))).thenReturn(author);

        AuthorDTO savedAuthorDTO = authorService.saveAuthor(authorDTO);

        assertEquals(1L, savedAuthorDTO.getId());
        assertEquals("Test Author", savedAuthorDTO.getName());
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void testGetAllAuthors() {
        Author author1 = new Author();
        author1.setId(1L);
        author1.setName("Author 1");

        Author author2 = new Author();
        author2.setId(2L);
        author2.setName("Author 2");

        when(authorRepository.findAll()).thenReturn(Arrays.asList(author1, author2));

        List<AuthorDTO> authors = authorService.getAllAuthors();

        assertEquals(2, authors.size());
        assertEquals("Author 1", authors.get(0).getName());
        assertEquals("Author 2", authors.get(1).getName());
    }

    @Test
    void testGetAuthorByIdWhenExistingId() {
        Long authorId = 1L;
        Author author = new Author();
        author.setId(authorId);
        author.setName("Test Author");

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        AuthorDTO authorDTO = authorService.getAuthorById(authorId);

        assertEquals(authorId, authorDTO.getId());
        assertEquals("Test Author", authorDTO.getName());
    }

    @Test
    void testGetAuthorByIdWhenNonExistingId() {
        Long authorId = 1L;

        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        AuthorDTO authorDTO = authorService.getAuthorById(authorId);

        assertEquals(null, authorDTO);
    }

    @Test
    void testGetAuthorsByName() {
        String name = "Author";
        Author author1 = new Author();
        author1.setId(1L);
        author1.setName("Author One");

        Author author2 = new Author();
        author2.setId(2L);
        author2.setName("Author Two");

        when(authorRepository.findByNameContainingIgnoreCase(name)).thenReturn(Arrays.asList(author1, author2));

        List<AuthorDTO> authors = authorService.getAuthorsByName(name);

        assertEquals(2, authors.size());
        assertEquals("Author One", authors.get(0).getName());
        assertEquals("Author Two", authors.get(1).getName());
    }

    @Test
    void testDeleteAuthorByIdWhenAuthorNotFound() {
        Long authorId = 1L;

        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authorService.deleteAuthorById(authorId);
        });

        assertEquals("Author not found", exception.getMessage());
    }

    @Test
    void testDeleteAuthorByIdWhenAuthorWithBooks() {
        Long authorId = 1L;
        Author author = new Author();
        author.setId(authorId);
        author.setName("Test Author");
        author.setBooks(Arrays.asList(new Book())); // Author has books

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authorService.deleteAuthorById(authorId);
        });

        assertEquals("Author cannot be deleted as it has associated books", exception.getMessage());
    }

    @Test
    void testDeleteAuthorByIdSuccess() {
        Long authorId = 1L;
        Author author = new Author();
        author.setId(authorId);
        author.setName("Test Author");
        author.setBooks(new ArrayList<>());

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        authorService.deleteAuthorById(authorId);

        verify(authorRepository, times(1)).delete(author);
    }
}
