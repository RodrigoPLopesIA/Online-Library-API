package br.com.rodrigo.onlinelibraryapi.repositories;

import br.com.rodrigo.onlinelibraryapi.entities.Author;
import br.com.rodrigo.onlinelibraryapi.entities.Book;
import br.com.rodrigo.onlinelibraryapi.entities.Genre;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest()
@DisplayName("Book Repository Test")
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private Book book;

    private Author author;

    @BeforeEach
    void setUp() {


        // Create a book instance
        book = new Book();
        book.setId(UUID.randomUUID());
        book.setTitle("Harry Potter and the Philosopher's Stone");
        book.setPublicationDate(LocalDate.of(1997, 6, 26));
        book.setGenre(Genre.Fiction);
        book.setPrice(BigDecimal.valueOf(29.99));
        book.setAuthor(author);
        book.setIsbn("978-0747532699");

    }

    @Test
    @DisplayName("Should save a book successfully")
    void shouldSaveBook() {
        author = authorRepository.findById(UUID.fromString("f161ecac-10d5-4b87-b0b5-aeee87e49258")).orElse(null);
        book.setAuthor(author);
        Book savedBook = bookRepository.save(book);
        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getId()).isEqualTo(book.getId());
    }

    @Test
    @DisplayName("Should find a book by id")
    void shouldFindById() {
        Optional<Book> found = bookRepository.findById(book.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Clean Code");
    }

    @Test
    @DisplayName("Should return empty when book not found by id")
    void shouldFindByIdNotFound() {
        Optional<Book> found = bookRepository.findById(UUID.randomUUID());
        assertThat(found).isNotPresent();
    }

    @Test
    @DisplayName("Should find all books")
    void shouldFindAll() {
        List<Book> books = bookRepository.findAll();
        assertThat(books).isNotEmpty();
        assertThat(books.get(0).getTitle()).isEqualTo("Clean Code");
    }

    @Test
    @DisplayName("Should delete a book")
    void shouldDeleteBook() {
        bookRepository.delete(book);
        Optional<Book> found = bookRepository.findById(book.getId());
        assertThat(found).isNotPresent();
    }

    @Test
    @DisplayName("Should update a book")
    void shouldUpdateBook() {
        book.setTitle("Clean Code - Updated");
        Book updatedBook = bookRepository.save(book);
        assertThat(updatedBook.getTitle()).isEqualTo("Clean Code - Updated");
    }
}
