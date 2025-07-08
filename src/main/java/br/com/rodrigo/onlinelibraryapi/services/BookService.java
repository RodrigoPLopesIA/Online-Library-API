package br.com.rodrigo.onlinelibraryapi.services;

import java.util.Arrays;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.rodrigo.onlinelibraryapi.dtos.books.CreateBookDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Author;
import br.com.rodrigo.onlinelibraryapi.entities.Book;
import br.com.rodrigo.onlinelibraryapi.entities.User;
import br.com.rodrigo.onlinelibraryapi.enums.Genre;
import br.com.rodrigo.onlinelibraryapi.exceptions.UnauthorizedException;
import br.com.rodrigo.onlinelibraryapi.repositories.BookRepository;
import br.com.rodrigo.onlinelibraryapi.repositories.specs.BookSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private UserService userService;

    public Page<Book> index(Pageable pageable, String title, String isbn, String authorName, Genre genre,
            String nationality) {

        Specification<Book> spec = BookSpecification.conjunction();

        if (title != null && !title.isBlank()) {
            spec = spec.and(BookSpecification.titleContains(title));
        }

        if (genre != null && !genre.toString().isBlank()) {
            spec = spec.and(BookSpecification.genreEquals(genre));
        }

        if (isbn != null && !isbn.isBlank()) {
            spec = spec.and(BookSpecification.isbnContains(isbn));
        }

        if (authorName != null && !authorName.isBlank()) {
            spec = spec.and(BookSpecification.authorNameContains(authorName));
        }

        if (nationality != null && !nationality.isBlank()) {
            spec = spec.and(BookSpecification.authorNationalityLike(nationality));
        }

        return bookRepository.findAll(spec, pageable);
    }

    public Book create(CreateBookDTO data, User authUser) {

        // verify if book alread exists by isbn
        if (this.existsByIsbn(data.isbn())) {
            throw new IllegalArgumentException("Book with ISBN " + data.isbn() + " already exists.");
        }

        // verify if book already exists by title
        if (this.existsByTitle(data.isbn())) {
            throw new IllegalArgumentException("Book with title " + data.title() + " already exists.");
        }

        // verify if author exists by id
        Author author = authorService.show(data.authorId());
        // verify if user exists by id
        User user = this.userService.findById(authUser.getId());


        Book book = Book.builder()
        .title(data.title())
        .isbn(data.isbn())
        .genre(data.genre())
        .author(author)
        .user(user)
        .publicationDate(data.publicationDate())
        .build();

        Book created = bookRepository.save(book);

        author.setBooks(Arrays.asList(created));
        user.setBooks(Arrays.asList(created));

        return created;
    }

    public Book update(UUID id, Book data, User authUser) {
        Book book = this.show(id);
        log.error("book user {}", book.getUser().getId());
        log.error("authUser user {}", authUser.getId());

        if (!book.getUser().getId().equals(authUser.getId())) {
            throw new UnauthorizedException("You are not authorized to perform this action on this book");
        }
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
        book.setAuthor(author);

        // verify if user exists by id
        User user = this.userService.findById(authUser.getId());
        data.setUser(user);
        
        
        book.update(data);

        Book updated = bookRepository.save(book);

        author.setBooks(Arrays.asList(updated));
        user.setBooks(Arrays.asList(updated));

        return book;
    }

    public Book show(UUID id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with ID " + id + " not found."));
    }

    public void delete(UUID id, User data) {

        User user = this.userService.findById(data.getId());
        Book book = this.show(id);
        if (!book.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You are not authorized to perform this action on this book");
        }
        bookRepository.delete(book);

    }

    public Boolean existsByIsbn(String isbn) {
        return bookRepository.existsByIsbn(isbn);
    }

    public Boolean existsByTitle(String title) {
        return bookRepository.existsByTitle(title);
    }

}
