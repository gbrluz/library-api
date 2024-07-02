package com.br.library_api.v1.service;

import com.br.library_api.domain.model.Author;
import com.br.library_api.domain.model.Book;
import com.br.library_api.domain.model.Rent;
import com.br.library_api.domain.model.dto.BookDTO;
import com.br.library_api.domain.repository.AuthorRepository;
import com.br.library_api.domain.repository.BookRepository;
import com.br.library_api.domain.repository.RentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private RentRepository rentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveBook() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setName("Test Book");
        bookDTO.setIsbn("1234567890");
        bookDTO.setPublicationDate(LocalDate.parse("2023-01-01"));
        bookDTO.setAuthorIds(List.of(1L, 2L));

        Author author1 = new Author();
        author1.setId(1L);
        Author author2 = new Author();
        author2.setId(2L);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author1));
        when(authorRepository.findById(2L)).thenReturn(Optional.of(author2));

        Book book = new Book();
        book.setName(bookDTO.getName());
        book.setIsbn(bookDTO.getIsbn());
        book.setPublicationDate(bookDTO.getPublicationDate());
        book.setAuthors(Stream.of(author1, author2).collect(Collectors.toList()));

        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book savedBook = invocation.getArgument(0);
            savedBook.setId(1L);
            return savedBook;
        });

        BookDTO savedBookDTO = bookService.saveBook(bookDTO);

        assertNotNull(savedBookDTO);
        assertEquals("Test Book", savedBookDTO.getName());
        assertEquals("1234567890", savedBookDTO.getIsbn());
        assertEquals(bookDTO.getPublicationDate(), savedBookDTO.getPublicationDate());
        assertEquals(2, savedBookDTO.getAuthorIds().size());
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).findById(2L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testGetAllBooks() {
        Author author1 = new Author();
        author1.setId(1L);
        Author author2 = new Author();
        author2.setId(2L);

        Book book1 = new Book();
        book1.setId(1L);
        book1.setName("Test Book 1");
        book1.setIsbn("1234567890");
        book1.setPublicationDate(LocalDate.parse("2023-01-01"));
        book1.setAuthors(List.of(author1));

        Book book2 = new Book();
        book2.setId(2L);
        book2.setName("Test Book 2");
        book2.setIsbn("0987654321");
        book2.setPublicationDate(LocalDate.parse("2023-01-01"));
        book2.setAuthors(List.of(author2));

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        List<BookDTO> bookDTOs = bookService.getAllBooks();

        assertNotNull(bookDTOs);
        assertEquals(2, bookDTOs.size());

        BookDTO bookDTO1 = bookDTOs.get(0);
        assertEquals("Test Book 1", bookDTO1.getName());
        assertEquals("1234567890", bookDTO1.getIsbn());
        assertEquals(book1.getPublicationDate(), bookDTO1.getPublicationDate());
        assertEquals(1, bookDTO1.getAuthorIds().size());
        assertEquals(1L, bookDTO1.getAuthorIds().get(0));

        BookDTO bookDTO2 = bookDTOs.get(1);
        assertEquals("Test Book 2", bookDTO2.getName());
        assertEquals("0987654321", bookDTO2.getIsbn());
        assertEquals(book2.getPublicationDate(), bookDTO2.getPublicationDate());
        assertEquals(1, bookDTO2.getAuthorIds().size());
        assertEquals(2L, bookDTO2.getAuthorIds().get(0));

        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetBookByIdIfBookExists() {
        Long bookId = 1L;
        Author author = new Author();
        author.setId(1L);

        Book book = new Book();
        book.setId(bookId);
        book.setName("Test Book");
        book.setIsbn("1234567890");
        book.setPublicationDate(LocalDate.parse("2023-01-01"));
        book.setAuthors(List.of(author));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        BookDTO bookDTO = bookService.getBookById(bookId);

        assertNotNull(bookDTO);
        assertEquals(bookId, bookDTO.getId());
        assertEquals("Test Book", bookDTO.getName());
        assertEquals("1234567890", bookDTO.getIsbn());
        assertEquals(book.getPublicationDate(), bookDTO.getPublicationDate());
        assertEquals(1, bookDTO.getAuthorIds().size());
        assertEquals(1L, bookDTO.getAuthorIds().get(0));

        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void testGetBookByIdIfBookDoesNotExist() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        BookDTO bookDTO = bookService.getBookById(bookId);

        assertNull(bookDTO);
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void testDeleteBook() {
        Long bookId = 1L;

        bookService.deleteBook(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void testGetAvailableBooks() {
        Author author = new Author();
        author.setId(1L);

        Book book1 = new Book();
        book1.setId(1L);
        book1.setName("Available Book 1");
        book1.setIsbn("1111111111");
        book1.setPublicationDate(LocalDate.parse("2023-01-01"));
        book1.setAuthors(List.of(author));

        Book book2 = new Book();
        book2.setId(2L);
        book2.setName("Available Book 2");
        book2.setIsbn("2222222222");
        book2.setPublicationDate(LocalDate.parse("2023-01-01"));
        book2.setAuthors(List.of(author));

        Book rentedBook = new Book();
        rentedBook.setId(3L);
        rentedBook.setName("Rented Book");
        rentedBook.setIsbn("3333333333");
        rentedBook.setPublicationDate(LocalDate.parse("2023-01-01"));
        rentedBook.setAuthors(List.of(author));

        Rent rent = new Rent();
        rent.setId(1L);
        rent.setBooks(List.of(rentedBook));

        when(rentRepository.findAll()).thenReturn(List.of(rent));
        when(bookRepository.findAll()).thenReturn(List.of(book1, book2, rentedBook));

        List<BookDTO> availableBooks = bookService.getAvailableBooks();

        assertNotNull(availableBooks);
        assertEquals(2, availableBooks.size());

        BookDTO availableBookDTO1 = availableBooks.get(0);
        assertEquals("Available Book 1", availableBookDTO1.getName());
        assertEquals("1111111111", availableBookDTO1.getIsbn());
        assertEquals(book1.getPublicationDate(), availableBookDTO1.getPublicationDate());
        assertEquals(1, availableBookDTO1.getAuthorIds().size());
        assertEquals(1L, availableBookDTO1.getAuthorIds().get(0));

        BookDTO availableBookDTO2 = availableBooks.get(1);
        assertEquals("Available Book 2", availableBookDTO2.getName());
        assertEquals("2222222222", availableBookDTO2.getIsbn());
        assertEquals(book2.getPublicationDate(), availableBookDTO2.getPublicationDate());
        assertEquals(1, availableBookDTO2.getAuthorIds().size());
        assertEquals(1L, availableBookDTO2.getAuthorIds().get(0));

        verify(rentRepository, times(1)).findAll();
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetRentedBooks() {
        Author author = new Author();
        author.setId(1L);

        Book rentedBook1 = new Book();
        rentedBook1.setId(1L);
        rentedBook1.setName("Rented Book 1");
        rentedBook1.setIsbn("1111111111");
        rentedBook1.setPublicationDate(LocalDate.parse("2023-01-01"));
        rentedBook1.setAuthors(List.of(author));

        Book rentedBook2 = new Book();
        rentedBook2.setId(2L);
        rentedBook2.setName("Rented Book 2");
        rentedBook2.setIsbn("2222222222");
        rentedBook2.setPublicationDate(LocalDate.parse("2023-01-01"));
        rentedBook2.setAuthors(List.of(author));

        Rent rent1 = new Rent();
        rent1.setId(1L);
        rent1.setBooks(List.of(rentedBook1));

        Rent rent2 = new Rent();
        rent2.setId(2L);
        rent2.setBooks(List.of(rentedBook2));

        Rent rent3 = new Rent();
        rent3.setId(3L);
        rent3.setBooks(List.of(rentedBook1)); // rentedBook1 is rented by multiple renters

        when(rentRepository.findAll()).thenReturn(List.of(rent1, rent2, rent3));

        List<BookDTO> rentedBooks = bookService.getRentedBooks();

        assertNotNull(rentedBooks);
        assertEquals(2, rentedBooks.size());

        BookDTO rentedBookDTO1 = rentedBooks.get(0);
        assertEquals("Rented Book 1", rentedBookDTO1.getName());
        assertEquals("1111111111", rentedBookDTO1.getIsbn());
        assertEquals(rentedBook1.getPublicationDate(), rentedBookDTO1.getPublicationDate());
        assertEquals(1, rentedBookDTO1.getAuthorIds().size());
        assertEquals(1L, rentedBookDTO1.getAuthorIds().get(0));

        BookDTO rentedBookDTO2 = rentedBooks.get(1);
        assertEquals("Rented Book 2", rentedBookDTO2.getName());
        assertEquals("2222222222", rentedBookDTO2.getIsbn());
        assertEquals(rentedBook2.getPublicationDate(), rentedBookDTO2.getPublicationDate());
        assertEquals(1, rentedBookDTO2.getAuthorIds().size());
        assertEquals(1L, rentedBookDTO2.getAuthorIds().get(0));

        verify(rentRepository, times(1)).findAll();
    }

    @Test
    void testGetBooksByAuthor() {
        Long authorId = 1L;
        Author author = new Author();
        author.setId(authorId);
        author.setName("Author Name");

        Book book1 = new Book();
        book1.setId(1L);
        book1.setName("Book 1");
        book1.setIsbn("1111111111");
        book1.setPublicationDate(LocalDate.parse("2023-01-01"));
        book1.setAuthors(List.of(author));

        Book book2 = new Book();
        book2.setId(2L);
        book2.setName("Book 2");
        book2.setIsbn("2222222222");
        book2.setPublicationDate(LocalDate.parse("2023-01-01"));
        book2.setAuthors(List.of(author));

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(bookRepository.findByAuthorsContaining(author)).thenReturn(List.of(book1, book2));

        List<BookDTO> booksByAuthor = bookService.getBooksByAuthor(authorId);

        assertNotNull(booksByAuthor);
        assertEquals(2, booksByAuthor.size());

        BookDTO bookDTO1 = booksByAuthor.get(0);
        assertEquals("Book 1", bookDTO1.getName());
        assertEquals("1111111111", bookDTO1.getIsbn());
        assertEquals(book1.getPublicationDate(), bookDTO1.getPublicationDate());
        assertEquals(1, bookDTO1.getAuthorIds().size());
        assertEquals(authorId, bookDTO1.getAuthorIds().get(0));

        BookDTO bookDTO2 = booksByAuthor.get(1);
        assertEquals("Book 2", bookDTO2.getName());
        assertEquals("2222222222", bookDTO2.getIsbn());
        assertEquals(book2.getPublicationDate(), bookDTO2.getPublicationDate());
        assertEquals(1, bookDTO2.getAuthorIds().size());
        assertEquals(authorId, bookDTO2.getAuthorIds().get(0));

        verify(authorRepository, times(1)).findById(authorId);
        verify(bookRepository, times(1)).findByAuthorsContaining(author);
    }

    @Test
    void testGetBooksByAuthorNotFound() {
        Long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookService.getBooksByAuthor(authorId);
        });

        assertEquals("Author not found", exception.getMessage());
        verify(authorRepository, times(1)).findById(authorId);
        verify(bookRepository, times(0)).findByAuthorsContaining(any(Author.class));
    }

    @Test
    void testDeleteBookIfNotRented() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setName("Test Book");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(rentRepository.findRentByBooks(book)).thenReturn(Collections.emptyList());

        bookService.deleteBookIfNotRented(bookId);

        verify(bookRepository, times(1)).findById(bookId);
        verify(rentRepository, times(1)).findRentByBooks(book);
        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    void testDeleteBookIfNotRentedWhenBookNotFound() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookService.deleteBookIfNotRented(bookId));
        verify(bookRepository, times(1)).findById(bookId);
        verify(rentRepository, times(0)).findRentByBooks(any(Book.class));
        verify(bookRepository, times(0)).delete(any(Book.class));
    }

    @Test
    void testDeleteBookIfNotRentedWhenBookIsRented() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setName("Test Book");

        Rent rent = new Rent();
        rent.setId(1L);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(rentRepository.findRentByBooks(book)).thenReturn(Collections.singletonList(rent));

        assertThrows(RuntimeException.class, () -> bookService.deleteBookIfNotRented(bookId));
        verify(bookRepository, times(1)).findById(bookId);
        verify(rentRepository, times(1)).findRentByBooks(book);
        verify(bookRepository, times(0)).delete(book);
    }



}
