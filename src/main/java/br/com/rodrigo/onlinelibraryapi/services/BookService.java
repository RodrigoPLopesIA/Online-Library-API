package br.com.rodrigo.onlinelibraryapi.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.rodrigo.onlinelibraryapi.dtos.books.CreateBookDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.books.ListBookDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Author;
import br.com.rodrigo.onlinelibraryapi.entities.Book;
import br.com.rodrigo.onlinelibraryapi.repositories.BookRepository;
import br.com.rodrigo.onlinelibraryapi.repositories.specs.BookSpecification;
import jakarta.persistence.EntityNotFoundException;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorService authorService;

    public Page<Book> index(Pageable pageable, String title, String isbn, String authorName) {

        Specification<Book> spec = Specification.where(null);

        if (title != null && !title.isBlank()) {
            spec = spec.and(BookSpecification.titleContains(title));
        }

        if (isbn != null && !isbn.isBlank()) {
            spec = spec.and(BookSpecification.isbnContains(isbn));
        }

        if (authorName != null && !authorName.isBlank()) {
            spec = spec.and(BookSpecification.authorNameContains(authorName));
        }

        return bookRepository.findAll(spec, pageable);
    }

    public Book create(Book data) {

        // verify if book alread exists by isbn
        if (this.existsByIsbn(data.getIsbn())) {
            throw new IllegalArgumentException("Book with ISBN " + data.getIsbn() + " already exists.");
        }

        // verify if book already exists by title
        if (this.existsByTitle(data.getTitle())) {
            throw new IllegalArgumentException("Book with title " + data.getTitle() + " already exists.");
        }

        // verify if author exists by id
        Author author = authorService.show(data.getAuthor().getId());
        data.setAuthor(author);

        return bookRepository.save(data);
    }

    public Book update(UUID id, Book data) {
        Book book = this.show(id);

        // verify if book already exists by isbn
        if (!book.getIsbn().equals(data.getIsbn()) && this.existsByIsbn(data.getIsbn())) {
            throw new IllegalArgumentException("Book with ISBN " + data.getIsbn() + " already exists.");
        }

        // verify if book already exists by title
        if (!book.getTitle().equals(data.getTitle()) && this.existsByTitle(data.getTitle())) {
            throw new IllegalArgumentException("Book with title " + data.getTitle() + " already exists.");
        }

        // verify if author exists by id
        Author author = authorService.show(data.getAuthor().getId());

        book.update(data);
        book.setAuthor(author);

        return bookRepository.save(book);
    }

    public Book show(UUID id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with ID " + id + " not found."));
    }

    public void delete(UUID id) {
        Book book = this.show(id);
        bookRepository.delete(book);

    }

    public Boolean existsByIsbn(String isbn) {
        return bookRepository.existsByIsbn(isbn);
    }

    public Boolean existsByTitle(String title) {
        return bookRepository.existsByTitle(title);
    }

}
