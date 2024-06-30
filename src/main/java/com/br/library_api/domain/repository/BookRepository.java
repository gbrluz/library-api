package com.br.library_api.domain.repository;

import com.br.library_api.domain.model.Author;
import com.br.library_api.domain.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByAuthorsContaining(Author author);
}
