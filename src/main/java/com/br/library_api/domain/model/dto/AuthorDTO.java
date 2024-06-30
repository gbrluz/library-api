package com.br.library_api.domain.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorDTO {
    private Long id;
    private String name;
    private String gender;
    private Integer birthYear;
    private String cpf;
}
