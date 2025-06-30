package br.com.rodrigo.onlinelibraryapi.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.rodrigo.onlinelibraryapi.entities.Author;
import br.com.rodrigo.onlinelibraryapi.repositories.AuthorRepository;

@Service
public class AuthorService {


    @Autowired
    private AuthorRepository authorRepository;


    public Author show(UUID id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author with ID " + id + " not found."));
    }
}
