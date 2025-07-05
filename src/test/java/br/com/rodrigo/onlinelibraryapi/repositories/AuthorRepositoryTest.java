package br.com.rodrigo.onlinelibraryapi.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

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

    @Test
    public void shouldUpdateAuthor() {
        UUID authorId = UUID.fromString("f161ecac-10d5-4b87-b0b5-aeee87e49258");
        Author author = repository.findById(authorId).orElse(null);

        author.setName("J.K. Rowling Updated");
        author.setUpdatedAt(java.time.Instant.now());
        Author updatedAuthor = repository.save(author);

        assertNotNull(updatedAuthor);  
        assertEquals(authorId, updatedAuthor.getId());
        assertEquals("J.K. Rowling Updated", updatedAuthor.getName());
        assertNotNull(updatedAuthor.getUpdatedAt());


    }

    @Test
    public void shouldFindAllAuthors() {
        List<Author> authors = repository.findAll();
        assertNotNull(authors);
    }

    @Test
    public void shouldFindAuthorById() {
        UUID authorId = UUID.fromString("f161ecac-10d5-4b87-b0b5-aeee87e49258"); 

        Author foundAuthor = repository.findById(authorId).orElse(null);    

        assertNotNull(foundAuthor);
        assertEquals(authorId, foundAuthor.getId());
        assertEquals("J.K. Rowling Updated", foundAuthor.getName());
        assertEquals(Date.valueOf("1965-07-31"), foundAuthor.getDateBirth());
        assertEquals("British", foundAuthor.getNationality());
        assertNotNull(foundAuthor.getCreatedAt());
        assertNotNull(foundAuthor.getUpdatedAt());

    }

    @Test
    public void shouldDeleteAuthor() {
        UUID authorId = UUID.fromString("f161ecac-10d5-4b87-b0b5-aeee87e49258");
        repository.deleteById(authorId);   
        Author deletedAuthor = repository.findById(authorId).orElse(null);
        assertEquals(null, deletedAuthor);   
    }
}
