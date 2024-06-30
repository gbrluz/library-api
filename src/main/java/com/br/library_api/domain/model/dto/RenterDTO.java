package com.br.library_api.domain.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RenterDTO {
    private Long id;
    private String name;
    private String gender;
    private String number;
    private String email;
    private LocalDate birthDate;
    private String cpf;
}
