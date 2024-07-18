package com.br.library_api.v1.service;

import com.br.library_api.client.GoogleBooksClient;
import com.br.library_api.client.GoogleBooksResponse;
import com.br.library_api.domain.model.Author;
import com.br.library_api.domain.model.Book;
import com.br.library_api.domain.model.dto.BookDTO;
import com.br.library_api.domain.repository.AuthorRepository;
import com.br.library_api.domain.repository.BookRepository;
import com.br.library_api.domain.repository.RentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private GoogleBooksClient googleBooksClient;

    public BookDTO saveBook(BookDTO bookDTO) {

        GoogleBooksResponse response = googleBooksClient.getBookByIsbn("isbn:" + bookDTO.getIsbn());

        if (response.getItems() == null || response.getItems().isEmpty()) {
            throw new RuntimeException("Invalid ISBN. Book not found in Google Books.");
        }
        Book book = new Book();
        book.setName(bookDTO.getName());
        book.setIsbn(bookDTO.getIsbn());
        book.setPublicationDate(bookDTO.getPublicationDate());

        List<Author> authors = bookDTO.getAuthorIds().stream()
                .map(id -> authorRepository.findById(id).orElse(null))
                .collect(Collectors.toList());

        book.setAuthors(authors);
        Book savedBook = bookRepository.save(book);

        return mapToDTO(savedBook);
    }

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public BookDTO getBookById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(this::mapToDTO).orElse(null);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public List<BookDTO> getAvailableBooks() {
        List<Book> rentedBooks = rentRepository.findAll().stream()
                .flatMap(rent -> rent.getBooks().stream())
                .collect(Collectors.toList());

        List<Book> availableBooks = bookRepository.findAll().stream()
                .filter(book -> !rentedBooks.contains(book))
                .collect(Collectors.toList());

        return availableBooks.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> getRentedBooks() {
        List<Book> rentedBooks = rentRepository.findAll().stream()
                .flatMap(rent -> rent.getBooks().stream())
                .distinct()
                .collect(Collectors.toList());

        return rentedBooks.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> getBooksByAuthor(Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        List<Book> books = bookRepository.findByAuthorsContaining(author);

        return books.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteBookIfNotRented(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        boolean isRented = !rentRepository.findRentByBooks(book).isEmpty();
        if (isRented) {
            throw new RuntimeException("Book cannot be deleted as it has been rented");
        }

        bookRepository.delete(book);
    }

    private BookDTO mapToDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setName(book.getName());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setPublicationDate(book.getPublicationDate());
        bookDTO.setAuthorIds(book.getAuthors().stream().map(Author::getId).collect(Collectors.toList()));
        return bookDTO;
    }

}
