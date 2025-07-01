package br.com.rodrigo.onlinelibraryapi.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.rodrigo.onlinelibraryapi.dtos.author.CreateAuthorDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Author;
import br.com.rodrigo.onlinelibraryapi.repositories.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class AuthorService {


    @Autowired
    private AuthorRepository authorRepository;

    public Author create(CreateAuthorDTO data) {

        if (this.existsByName(data.name())) {
            throw new IllegalArgumentException("Author with name " + data.name() + " already exists.");
        }

        Author author = new Author(data);
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
}
