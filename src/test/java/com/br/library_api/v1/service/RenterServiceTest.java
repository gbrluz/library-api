package com.br.library_api.v1.service;

import com.br.library_api.domain.model.Rent;
import com.br.library_api.domain.model.Renter;
import com.br.library_api.domain.model.dto.RenterDTO;
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
public class RenterServiceTest {
    @Mock
    private RenterRepository renterRepository;

    @Mock
    private RentRepository rentRepository;

    @InjectMocks
    private RenterService renterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveRenter() {
        RenterDTO renterDTO = new RenterDTO();
        renterDTO.setName("Gabriel Luz");
        renterDTO.setGender("Male");
        renterDTO.setNumber("123456789");
        renterDTO.setEmail("gabriel.luz@example.com");
        renterDTO.setBirthDate(LocalDate.of(1990, 5, 15));
        renterDTO.setCpf("123.456.789-00");

        Renter renter = new Renter();
        renter.setId(1L);
        renter.setName(renterDTO.getName());
        renter.setGender(renterDTO.getGender());
        renter.setNumber(renterDTO.getNumber());
        renter.setEmail(renterDTO.getEmail());
        renter.setBirthDate(renterDTO.getBirthDate());
        renter.setCpf(renterDTO.getCpf());

        when(renterRepository.save(any(Renter.class))).thenReturn(renter);

        RenterDTO savedRenterDTO = renterService.saveRenter(renterDTO);

        assertNotNull(savedRenterDTO);
        assertEquals(1L, savedRenterDTO.getId());
        assertEquals("Gabriel Luz", savedRenterDTO.getName());
        assertEquals("Male", savedRenterDTO.getGender());
        assertEquals("123456789", savedRenterDTO.getNumber());
        assertEquals("gabriel.luz@example.com", savedRenterDTO.getEmail());
        assertEquals(LocalDate.of(1990, 5, 15), savedRenterDTO.getBirthDate());
        assertEquals("123.456.789-00", savedRenterDTO.getCpf());

        verify(renterRepository, times(1)).save(any(Renter.class));
    }

    @Test
    void testGetAllRenters() {
        Renter renter1 = new Renter();
        renter1.setId(1L);
        renter1.setName("Gabriel Luz");

        Renter renter2 = new Renter();
        renter2.setId(2L);
        renter2.setName("Gabriel Miranda");

        when(renterRepository.findAll()).thenReturn(Arrays.asList(renter1, renter2));

        List<RenterDTO> renters = renterService.getAllRenters();

        assertEquals(2, renters.size());
        assertEquals(1L, renters.get(0).getId());
        assertEquals("Gabriel Luz", renters.get(0).getName());
        assertEquals(2L, renters.get(1).getId());
        assertEquals("Gabriel Miranda", renters.get(1).getName());

        verify(renterRepository, times(1)).findAll();
    }

    @Test
    void testGetRenterById() {
        Long renterId = 1L;
        Renter renter = new Renter();
        renter.setId(renterId);
        renter.setName("Gabriel Luz");

        when(renterRepository.findById(renterId)).thenReturn(Optional.of(renter));

        RenterDTO renterDTO = renterService.getRenterById(renterId);

        assertNotNull(renterDTO);
        assertEquals(renterId, renterDTO.getId());
        assertEquals("Gabriel Luz", renterDTO.getName());

        verify(renterRepository, times(1)).findById(renterId);
    }

    @Test
    void testGetRenterByIdWhenNotFound() {
        Long renterId = 1L;

        when(renterRepository.findById(renterId)).thenReturn(Optional.empty());

        RenterDTO renterDTO = renterService.getRenterById(renterId);

        assertNull(renterDTO);

        verify(renterRepository, times(1)).findById(renterId);
    }

    @Test
    void testDeleteRenterWhenRenterHasActiveRent() {
        Long renterId = 1L;
        Renter renter = new Renter();
        renter.setId(renterId);
        renter.setName("Gabriel Luz");

        when(renterRepository.findById(renterId)).thenReturn(Optional.of(renter));
        when(rentRepository.findByRenter(renter)).thenReturn(Arrays.asList(new Rent(), new Rent()));

        assertThrows(RuntimeException.class, () -> renterService.deleteRenter(renterId));

        verify(renterRepository, times(1)).findById(renterId);
        verify(rentRepository, never()).delete(any(Rent.class));
    }

    @Test
    void testDeleteRenterWhenSuccessDeleted() {
        Long renterId = 1L;
        Renter renter = new Renter();
        renter.setId(renterId);
        renter.setName("Gabriel Luz");

        when(renterRepository.findById(renterId)).thenReturn(Optional.of(renter));
        when(rentRepository.findByRenter(renter)).thenReturn(Arrays.asList());

        renterService.deleteRenter(renterId);

        verify(renterRepository, times(1)).findById(renterId);
        verify(rentRepository, times(1)).findByRenter(renter);
        verify(renterRepository, times(1)).delete(renter);
    }
}
