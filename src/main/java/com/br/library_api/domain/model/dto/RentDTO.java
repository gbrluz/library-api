package com.br.library_api.domain.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class RentDTO {
    private Long id;
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private List<Long> bookIds;
    private Long renterId;
}
