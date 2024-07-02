package com.br.library_api.domain.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BookDTO {
    private Long id;
    private String name;
    private String isbn;
    private LocalDate publicationDate;
    private List<Long> authorIds;
}
