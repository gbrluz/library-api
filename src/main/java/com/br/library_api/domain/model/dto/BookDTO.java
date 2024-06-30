package com.br.library_api.domain.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class BookDTO {
    private Long id;
    private String name;
    private String isbn;
    private Date publicationDate;
    private List<Long> authorIds;
}
