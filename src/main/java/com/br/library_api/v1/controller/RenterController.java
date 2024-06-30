package com.br.library_api.v1.controller;

import com.br.library_api.domain.model.dto.RenterDTO;
import com.br.library_api.v1.service.RenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/renters")
public class RenterController {

    @Autowired
    private RenterService renterService;

    @PostMapping
    public ResponseEntity<RenterDTO> createRenter(@RequestBody RenterDTO renterDTO) {
        RenterDTO savedRenterDTO = renterService.saveRenter(renterDTO);
        return ResponseEntity.ok(savedRenterDTO);
    }

    @GetMapping
    public ResponseEntity<List<RenterDTO>> getAllRenters() {
        List<RenterDTO> renters = renterService.getAllRenters();
        return ResponseEntity.ok(renters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RenterDTO> getRenterById(@PathVariable Long id) {
        RenterDTO renterDTO = renterService.getRenterById(id);
        return (renterDTO != null) ? ResponseEntity.ok(renterDTO) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRenter(@PathVariable Long id) {
        try {
            renterService.deleteRenter(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
