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

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    private static final double LATE_FEE_PER_DAY = 1.0;
    private static final double RENTAL_FEE_PER_DAY = 2.0;

    public RentDTO saveRent(RentDTO rentDTO) {
        Rent rent = new Rent();
        rent.setPickupDate(rentDTO.getPickupDate());
        rent.setReturnDate(rentDTO.getReturnDate());

        List<Book> books = bookRepository.findAllById(rentDTO.getBookIds());
        rent.setBooks(books);

        Renter renter = renterRepository.findById(rentDTO.getRenterId())
                .orElseThrow(() -> new RuntimeException("Renter not found"));
        rent.setRenter(renter);

        rent.setRentalFee(calculateRentalFee(rent.getPickupDate(), rent.getReturnDate()));
        rent.setLateFee(calculateLateFee(rent.getReturnDate()));

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
        rentDTO.setLateFee(rent.getLateFee());
        rentDTO.setRentalFee(rent.getRentalFee());
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

    private Double calculateRentalFee(LocalDate pickupDate, LocalDate returnDate) {
        if (pickupDate == null || returnDate == null || returnDate.isBefore(pickupDate)) {
            return 0.0;
        }
        long daysRented = ChronoUnit.DAYS.between(pickupDate, returnDate);
        return daysRented * 2.0;
    }

    private Double calculateLateFee(LocalDate returnDate) {
        if (returnDate == null || returnDate.isBefore(LocalDate.now())) {
            return 0.0;
        }
        long daysLate = ChronoUnit.DAYS.between(LocalDate.now(), returnDate);
        return daysLate * 1.0;
    }
}
