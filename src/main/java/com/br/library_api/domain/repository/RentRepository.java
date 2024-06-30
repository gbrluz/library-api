package com.br.library_api.domain.repository;

import com.br.library_api.domain.model.Book;
import com.br.library_api.domain.model.Rent;
import com.br.library_api.domain.model.Renter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {

    List<Rent> findByRenter(Renter renter);

    List<Rent> findRentByBooks(Book book);
}
