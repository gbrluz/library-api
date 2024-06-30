package com.br.library_api.v1.service;

import com.br.library_api.domain.model.Author;
import com.br.library_api.domain.model.dto.AuthorDTO;
import com.br.library_api.domain.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public AuthorDTO saveAuthor(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setName(authorDTO.getName());
        author.setGender(authorDTO.getGender());
        author.setBirthYear(authorDTO.getBirthYear());
        author.setCpf(authorDTO.getCpf());

        Author savedAuthor = authorRepository.save(author);
        return mapToDTO(savedAuthor);
    }

    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public AuthorDTO getAuthorById(Long id) {
        Optional<Author> author = authorRepository.findById(id);
        return author.map(this::mapToDTO).orElse(null);
    }

    public List<AuthorDTO> getAuthorsByName(String name) {
        List<Author> authors = authorRepository.findByNameContainingIgnoreCase(name);
        return authors.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void deleteAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        if (!author.getBooks().isEmpty()) {
            throw new RuntimeException("Author cannot be deleted as it has associated books");
        }

        authorRepository.delete(author);
    }

    private AuthorDTO mapToDTO(Author author) {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setName(author.getName());
        authorDTO.setGender(author.getGender());
        authorDTO.setBirthYear(author.getBirthYear());
        authorDTO.setCpf(author.getCpf());
        return authorDTO;
    }
}
