package br.com.rodrigo.onlinelibraryapi.services;

import java.sql.Date;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import br.com.rodrigo.onlinelibraryapi.dtos.author.CreateAuthorDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.author.ListAuthorDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Author;
import br.com.rodrigo.onlinelibraryapi.repositories.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public Page<ListAuthorDTO> index(Pageable pageable, String name, String nationality, Date dateBirth) {
        Author author = new Author();
        author.setName(name);
        author.setDateBirth(dateBirth);
        author.setNationality(nationality);

        var matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Author> example = Example.of(author, matcher);

        Page<Author> authors = authorRepository.findAll(example, pageable);
        return authors.map(ListAuthorDTO::new);
    }

    public Author create(CreateAuthorDTO data) {

        if (this.existsByName(data.name())) {
            throw new IllegalArgumentException("Author with name " + data.name() + " already exists.");
        }

        Author author = new Author(data);
        return authorRepository.save(author);
    }

    public Author update(UUID id, CreateAuthorDTO data) {
        
        Author author = this.show(id);
        author.update(data);

        if (!author.getName().equals(data.name()) && this.existsByName(data.name())) {
            throw new IllegalArgumentException("Author with name " + data.name() + " already exists.");
        }


        return authorRepository.save(author);
    }

    public Author show(UUID id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author with ID " + id + " not found."));
    }

    public Author findByName(String name) {
        return authorRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Author with name " + name + " not found."));
    }

    public boolean existsById(UUID id) {
        return authorRepository.existsById(id);
    }

    public boolean existsByName(String name) {
        return authorRepository.findByName(name).isPresent();
    }

    public void delete(UUID id) {
        Author author = this.show(id);
        this.authorRepository.delete(author);
    }
}
