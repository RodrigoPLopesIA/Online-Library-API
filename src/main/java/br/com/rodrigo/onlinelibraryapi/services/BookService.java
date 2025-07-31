package br.com.rodrigo.onlinelibraryapi.services;

import java.util.Arrays;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.rodrigo.onlinelibraryapi.dtos.books.CreateBookDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.category.ListCategoryDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.files.UploadFileDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Author;
import br.com.rodrigo.onlinelibraryapi.entities.Book;
import br.com.rodrigo.onlinelibraryapi.entities.Category;
import br.com.rodrigo.onlinelibraryapi.entities.User;
import br.com.rodrigo.onlinelibraryapi.enums.Genre;
import br.com.rodrigo.onlinelibraryapi.exceptions.UnauthorizedException;
import br.com.rodrigo.onlinelibraryapi.exceptions.UniqueViolationException;
import br.com.rodrigo.onlinelibraryapi.patterns.factory.BookFactory;
import br.com.rodrigo.onlinelibraryapi.patterns.strategy.BookEmailContextStrategy;
import br.com.rodrigo.onlinelibraryapi.patterns.strategy.DocumentValidationStrategy;
import br.com.rodrigo.onlinelibraryapi.repositories.BookRepository;
import br.com.rodrigo.onlinelibraryapi.repositories.specs.BookSpecification;
import jakarta.mail.MessagingException;
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

    @Autowired
    private CategoryService categoryService;

    @Autowired
    StorageService storageService;

    @Autowired
    EmailService emailService;

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

    public Book create(CreateBookDTO data, User authUser) throws MessagingException {

        // verify if book alread exists by isbn
        if (this.existsByIsbn(data.isbn())) {
            throw new IllegalArgumentException("Book with ISBN " + data.isbn() + " already exists.");
        }

        // verify if book already exists by title
        if (this.existsByTitle(data.title())) {
            throw new IllegalArgumentException("Book with title " + data.title() + " already exists.");
        }

        // verify if category exists by id
        Category category = categoryService.show(data.genreId());
        ;

        // verify if author exists by id
        Author author = authorService.show(UUID.fromString(data.authorId()));
        // verify if user exists by id
        User user = userService.findById(authUser.getId());

        Book book = BookFactory.createFrom(data);

        book.setAuthor(author);
        book.setUser(user);
        book.setCategories(Arrays.asList(category));

        Book created = bookRepository.save(book);

        author.setBooks(Arrays.asList(book));
        user.setBooks(Arrays.asList(book));
        category.setBook(book);

        emailService.send(user.getAuthentication().getEmail(), "Book created!", "mail/book-created",
                new BookEmailContextStrategy(book));
        return created;
    }

    public Book update(UUID id, Book data, User authUser) throws MessagingException {
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

        book = BookFactory.createFrom(data);
        book.setAuthor(author);
        book.setUser(user);

        Book updated = bookRepository.save(book);

        author.setBooks(Arrays.asList(updated));
        user.setBooks(Arrays.asList(updated));

        emailService.send(user.getAuthentication().getEmail(), "Book updated!", "mail/book-updated",
                new BookEmailContextStrategy(book));

        return book;
    }

    public Book show(UUID id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with ID " + id + " not found."));
    }

    public void delete(UUID id, User data) throws MessagingException {

        User user = this.userService.findById(data.getId());
        Book book = this.show(id);
        if (!book.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You are not authorized to perform this action on this book");
        }
        bookRepository.delete(book);
        emailService.send(user.getAuthentication().getEmail(), "Book deleted!", "mail/book-deleted",
                new BookEmailContextStrategy(book));

    }

    public Boolean existsByIsbn(String isbn) {
        return bookRepository.existsByIsbn(isbn);
    }

    public Boolean existsByTitle(String title) {
        return bookRepository.existsByTitle(title);
    }

    public UploadFileDTO uploadBookFile(UUID bookId, User data, MultipartFile file) {
        try {

            User user = this.userService.findById(data.getId());
            Book book = this.show(bookId);

            if (!user.getId().equals(book.getUser().getId())) {
                throw new UnauthorizedException("You are not authorized to perform this action on this book");
            }

            this.storageService.validateFile(file, new DocumentValidationStrategy());

            String bucketName = "book";
            String objectName = this.storageService.generateFileName(
                    user.getName().getFirst_name() + "_" + user.getName().getLast_name() + "_" + book.getTitle(),
                    file.getOriginalFilename());
            String contentType = file.getContentType();

            storageService.uploadFile(
                    bucketName,
                    objectName,
                    file.getInputStream(),
                    contentType);
            String url_image = storageService.getFileUrl(objectName, bucketName);

            book.setBookFile(url_image);

            this.bookRepository.save(book);

            return new UploadFileDTO("Upload successfully!", url_image);

        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new UniqueViolationException("Error: " + e.getMessage());
        }
    }
}
