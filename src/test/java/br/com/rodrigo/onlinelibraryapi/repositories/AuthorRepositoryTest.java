package br.com.rodrigo.onlinelibraryapi.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.rodrigo.onlinelibraryapi.entities.Author;


@SpringBootTest
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository repository;


    @Test
    public void shouldSaveAuthor() {
        // Given
        Author author = new Author();
        author.setName("J.K. Rowling");
        author.setDateBirth(Date.valueOf("1965-07-31"));
        author.setNationality("British");
        author.setCreatedAt(java.time.Instant.now());
        author.setUpdatedAt(java.time.Instant.now());

        // When
        Author savedAuthor = repository.save(author);

        // Then
        assertNotNull(savedAuthor.getId());
        assertEquals("J.K. Rowling", savedAuthor.getName());
    }
}
