package com.br.library_api.v1.service;

import com.br.library_api.domain.model.Renter;
import com.br.library_api.domain.model.dto.RenterDTO;
import com.br.library_api.domain.repository.RentRepository;
import com.br.library_api.domain.repository.RenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RenterService {

    @Autowired
    private RenterRepository renterRepository;

    @Autowired
    private RentRepository rentRepository;

    public RenterDTO saveRenter(RenterDTO renterDTO) {
        Renter renter = new Renter();
        renter.setName(renterDTO.getName());
        renter.setGender(renterDTO.getGender());
        renter.setNumber(renterDTO.getNumber());
        renter.setEmail(renterDTO.getEmail());
        renter.setBirthDate(renterDTO.getBirthDate());
        renter.setCpf(renterDTO.getCpf());
        Renter savedRenter = renterRepository.save(renter);
        return mapToDTO(savedRenter);
    }

    public List<RenterDTO> getAllRenters() {
        return renterRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public RenterDTO getRenterById(Long id) {
        Optional<Renter> renter = renterRepository.findById(id);
        return renter.map(this::mapToDTO).orElse(null);
    }

    public void deleteRenter(Long id) {
        Renter renter = renterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Renter not found"));

        boolean hasActiveRents = rentRepository.findByRenter(renter).stream()
                .anyMatch(rent -> rent.getReturnDate() == null || rent.getReturnDate().isAfter(LocalDate.now()));
        if (hasActiveRents) {
            throw new RuntimeException("Renter cannot be deleted as they have active rentals");
        }

        renterRepository.delete(renter);
    }


    private RenterDTO mapToDTO(Renter renter) {
        RenterDTO renterDTO = new RenterDTO();
        renterDTO.setId(renter.getId());
        renterDTO.setName(renter.getName());
        renterDTO.setGender(renter.getGender());
        renterDTO.setNumber(renter.getNumber());
        renterDTO.setEmail(renter.getEmail());
        renterDTO.setBirthDate(renter.getBirthDate());
        renterDTO.setCpf(renter.getCpf());
        return renterDTO;
    }
}
