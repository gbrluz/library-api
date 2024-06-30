package com.br.library_api.v1.controller;

import com.br.library_api.domain.model.dto.BookDTO;
import com.br.library_api.domain.model.dto.RentDTO;
import com.br.library_api.v1.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rents")
public class RentController {

    @Autowired
    private RentService rentService;

    @PostMapping
    public ResponseEntity<RentDTO> createRent(@RequestBody RentDTO rentDTO) {
        RentDTO savedRentDTO = rentService.saveRent(rentDTO);
        return ResponseEntity.ok(savedRentDTO);
    }

    @GetMapping
    public ResponseEntity<List<RentDTO>> getAllRents() {
        List<RentDTO> rents = rentService.getAllRents();
        return ResponseEntity.ok(rents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentDTO> getRentById(@PathVariable Long id) {
        RentDTO rentDTO = rentService.getRentById(id);
        return (rentDTO != null) ? ResponseEntity.ok(rentDTO) : ResponseEntity.notFound().build();
    }

    @GetMapping("/renter/{renterId}/books")
    public ResponseEntity<List<BookDTO>> getBooksRentedByRenter(@PathVariable Long renterId) {
        List<BookDTO> rentedBooks = rentService.getBooksRentedByRenter(renterId);
        return ResponseEntity.ok(rentedBooks);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRent(@PathVariable Long id) {
        rentService.deleteRent(id);
        return ResponseEntity.noContent().build();
    }
}
