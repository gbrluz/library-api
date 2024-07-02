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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RentServiceTest {
    @Mock
    private RentRepository rentRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private RenterRepository renterRepository;

    @InjectMocks
    private RentService rentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveRent() {
        RentDTO rentDTO = new RentDTO();
        rentDTO.setPickupDate(LocalDate.now());
        rentDTO.setReturnDate(LocalDate.now().plusDays(2));
        rentDTO.setBookIds(Arrays.asList(1L, 2L));
        rentDTO.setRenterId(1L);

        Book book1 = new Book();
        book1.setId(1L);
        Book book2 = new Book();
        book2.setId(2L);

        Renter renter = new Renter();
        renter.setId(1L);

        when(bookRepository.findAllById(rentDTO.getBookIds())).thenReturn(Arrays.asList(book1, book2));
        when(renterRepository.findById(rentDTO.getRenterId())).thenReturn(Optional.of(renter));

        Rent savedRent = new Rent();
        savedRent.setId(1L);
        savedRent.setPickupDate(rentDTO.getPickupDate());
        savedRent.setReturnDate(rentDTO.getReturnDate());
        savedRent.setBooks(Arrays.asList(book1, book2));
        savedRent.setRenter(renter);

        when(rentRepository.save(any(Rent.class))).thenReturn(savedRent);

        RentDTO savedRentDTO = rentService.saveRent(rentDTO);

        assertNotNull(savedRentDTO);
        assertEquals(1L, savedRentDTO.getId());
        assertEquals(rentDTO.getPickupDate(), savedRentDTO.getPickupDate());
        assertEquals(rentDTO.getReturnDate(), savedRentDTO.getReturnDate());
        assertEquals(rentDTO.getBookIds(), savedRentDTO.getBookIds());
        assertEquals(rentDTO.getRenterId(), savedRentDTO.getRenterId());

        verify(bookRepository, times(1)).findAllById(rentDTO.getBookIds());
        verify(renterRepository, times(1)).findById(rentDTO.getRenterId());
        verify(rentRepository, times(1)).save(any(Rent.class));
    }

    @Test
    void testGetAllRents() {
        Book book1 = new Book();
        book1.setId(1L);
        Book book2 = new Book();
        book2.setId(2L);

        Renter renter = new Renter();
        renter.setId(1L);

        Rent rent1 = new Rent();
        rent1.setId(1L);
        rent1.setPickupDate(LocalDate.now());
        rent1.setReturnDate(LocalDate.now().plusDays(2));
        rent1.setBooks(Arrays.asList(book1, book2));
        rent1.setRenter(renter);

        Rent rent2 = new Rent();
        rent2.setId(2L);
        rent2.setPickupDate(LocalDate.now());
        rent2.setReturnDate(LocalDate.now().plusDays(2));
        rent2.setBooks(Arrays.asList(book1)); // Example with one book
        rent2.setRenter(renter);

        when(rentRepository.findAll()).thenReturn(Arrays.asList(rent1, rent2));

        List<RentDTO> rents = rentService.getAllRents();

        assertEquals(2, rents.size());

        RentDTO rentDTO1 = rents.get(0);
        assertNotNull(rentDTO1);
        assertEquals(1L, rentDTO1.getId());
        assertEquals(rent1.getPickupDate(), rentDTO1.getPickupDate());
        assertEquals(rent1.getReturnDate(), rentDTO1.getReturnDate());
        assertEquals(Arrays.asList(1L, 2L), rentDTO1.getBookIds());
        assertEquals(1L, rentDTO1.getRenterId());

        RentDTO rentDTO2 = rents.get(1);
        assertNotNull(rentDTO2);
        assertEquals(2L, rentDTO2.getId());
        assertEquals(rent2.getPickupDate(), rentDTO2.getPickupDate());
        assertEquals(rent2.getReturnDate(), rentDTO2.getReturnDate());
        assertEquals(Arrays.asList(1L), rentDTO2.getBookIds());
        assertEquals(1L, rentDTO2.getRenterId());

        verify(rentRepository, times(1)).findAll();
    }

    @Test
    void testGetRentById() {
        Long rentId = 1L;

        Renter renter = new Renter();
        renter.setId(1L);

        Author author1 = new Author();
        author1.setId(1L);
        author1.setName("Author 1");

        Book book1 = new Book();
        book1.setId(1L);
        book1.setName("Book 1");
        book1.setAuthors(List.of(author1));

        Rent rent = new Rent();
        rent.setId(rentId);
        rent.setPickupDate(LocalDate.now());
        rent.setReturnDate(LocalDate.now().plusDays(2));
        rent.setRenter(renter);
        rent.setBooks(List.of(book1));

        when(rentRepository.findById(rentId)).thenReturn(Optional.of(rent));

        RentDTO rentDTO = rentService.getRentById(rentId);

        assertEquals(rentId, rentDTO.getId());
        assertEquals(1, rentDTO.getBookIds().size());

        verify(rentRepository, times(1)).findById(rentId);
    }

    @Test
    void testGetRentByIdWhenRentNotFound() {
        Long rentId = 1L;

        when(rentRepository.findById(rentId)).thenReturn(Optional.empty());

        RentDTO rentDTO = rentService.getRentById(rentId);

        assertNull(rentDTO);

        verify(rentRepository, times(1)).findById(rentId);
    }

    @Test
    void testGetBooksRentedByRenter() {
        Renter renter = new Renter();
        renter.setId(1L);

        Author author1 = new Author();
        author1.setId(1L);
        author1.setName("Author 1");

        Book book1 = new Book();
        book1.setId(1L);
        book1.setName("Book 1");
        book1.setAuthors(List.of(author1));

        Book book2 = new Book();
        book2.setId(2L);
        book2.setName("Book 2");
        book2.setAuthors(List.of(author1));

        Rent rent1 = new Rent();
        rent1.setId(1L);
        rent1.setPickupDate(LocalDate.now());
        rent1.setReturnDate(LocalDate.now().plusDays(2));
        rent1.setRenter(renter);
        rent1.setBooks(List.of(book1, book2));

        when(renterRepository.findById(renter.getId())).thenReturn(Optional.of(renter));
        when(rentRepository.findByRenter(renter)).thenReturn(List.of(rent1));

        List<BookDTO> rentedBooks = rentService.getBooksRentedByRenter(renter.getId());

        assertEquals(2, rentedBooks.size());
        assertEquals(1L, rentedBooks.get(0).getId());
        assertEquals("Book 1", rentedBooks.get(0).getName());
        assertEquals(2L, rentedBooks.get(1).getId());
        assertEquals("Book 2", rentedBooks.get(1).getName());

        verify(renterRepository, times(1)).findById(renter.getId());
        verify(rentRepository, times(1)).findByRenter(renter);
    }

    @Test
    void testDeleteRent() {
        Long rentId = 1L;

        rentService.deleteRent(rentId);

        verify(rentRepository, times(1)).deleteById(rentId);
    }
}
