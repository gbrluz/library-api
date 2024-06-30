package com.br.library_api.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity(name = "rent")
@Table(name = "rent")
@Getter
@Setter
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate pickupDate;

    @Column
    private LocalDate returnDate;

    @ManyToMany
    @JoinTable(
            name = "rent_book",
            joinColumns = @JoinColumn(name = "rent_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> books;

    @ManyToOne
    @JoinColumn(name = "renter_id")
    private Renter renter;

}
