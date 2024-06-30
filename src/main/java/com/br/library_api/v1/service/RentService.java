package com.br.library_api.v1.service;

import com.br.library_api.domain.model.Author;
import com.br.library_api.domain.model.Book;
import com.br.library_api.domain.model.Rent;
import com.br.library_api.domain.model.Renter;
import com.br.library_api.domain.model.dto.BookDTO;
import com.br.library_api.domain.model.dto.RentDTO;
import com.br.library_api.domain.repository.BookRepository;
import com.br.library_api.domain.repository.RentRepository;
import com.br.library_api.domain.repository.RenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RentService {

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RenterRepository renterRepository;

    public RentDTO saveRent(RentDTO rentDTO) {
        Rent rent = new Rent();
        rent.setPickupDate(rentDTO.getPickupDate());
        rent.setReturnDate(rentDTO.getReturnDate());

        List<Book> books = bookRepository.findAllById(rentDTO.getBookIds());
        rent.setBooks(books);

        Renter renter = renterRepository.findById(rentDTO.getRenterId())
                .orElseThrow(() -> new RuntimeException("Renter not found"));
        rent.setRenter(renter);

        Rent savedRent = rentRepository.save(rent);
        return mapToDTO(savedRent);
    }

    public List<RentDTO> getAllRents() {
        return rentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public RentDTO getRentById(Long id) {
        Optional<Rent> rent = rentRepository.findById(id);
        return rent.map(this::mapToDTO).orElse(null);
    }

    public List<BookDTO> getBooksRentedByRenter(Long renterId) {
        Renter renter = renterRepository.findById(renterId)
                .orElseThrow(() -> new RuntimeException("Renter not found"));

        List<Book> rentedBooks = rentRepository.findByRenter(renter).stream()
                .flatMap(rent -> rent.getBooks().stream())
                .distinct()
                .collect(Collectors.toList());

        return rentedBooks.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void deleteRent(Long id) {
        rentRepository.deleteById(id);
    }

    private RentDTO mapToDTO(Rent rent) {
        RentDTO rentDTO = new RentDTO();
        rentDTO.setId(rent.getId());
        rentDTO.setPickupDate(rent.getPickupDate());
        rentDTO.setReturnDate(rent.getReturnDate());
        rentDTO.setBookIds(rent.getBooks().stream().map(Book::getId).collect(Collectors.toList()));
        rentDTO.setRenterId(rent.getRenter().getId());
        return rentDTO;
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
