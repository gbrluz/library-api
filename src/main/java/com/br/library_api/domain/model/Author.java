package com.br.library_api.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity(name = "author")
@Table(name = "author")
@Getter
@Setter
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String gender;

    @Column(nullable = false)
    private Integer birthYear;

    @Column(nullable = false, unique = true)
    private String cpf;

    @ManyToMany(mappedBy = "authors")
    private List<Book> books;
}
