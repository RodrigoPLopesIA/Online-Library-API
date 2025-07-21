package br.com.rodrigo.onlinelibraryapi.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import br.com.rodrigo.onlinelibraryapi.dtos.books.CreateBookDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.files.UploadFileDTO;
import br.com.rodrigo.onlinelibraryapi.entities.*;
import br.com.rodrigo.onlinelibraryapi.entities.embedded.Authentication;
import br.com.rodrigo.onlinelibraryapi.entities.embedded.Name;
import br.com.rodrigo.onlinelibraryapi.enums.Genre;
import br.com.rodrigo.onlinelibraryapi.exceptions.UnauthorizedException;
import br.com.rodrigo.onlinelibraryapi.exceptions.UniqueViolationException;
import br.com.rodrigo.onlinelibraryapi.patterns.factory.BookFactory;
import br.com.rodrigo.onlinelibraryapi.patterns.strategy.BookEmailContextStrategy;
import br.com.rodrigo.onlinelibraryapi.repositories.BookRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorService authorService;

    @Mock
    private UserService userService;

    @Mock
    private StorageService storageService;

    @Mock
    private EmailService emailService;

    @Mock
    private MultipartFile file;

    private AutoCloseable closeable;

    private UUID bookId;
    private User user;
    private Author author;
    private Book book;
    private CreateBookDTO createBookDTO;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        bookId = UUID.randomUUID();

        user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setAuthentication(new Authentication("user@test.com", "23123458678971"));
        user.setName(new Name("John", "Doe"));

        author = Author.builder().id(UUID.randomUUID()).name("Author").build();
        Category genre = Category.builder().id(UUID.randomUUID().toString()).name("test").build();
        ;
        book = Book.builder()
                .id(bookId)
                .title("Book Title")
                .isbn("123")
                .author(author)
                .user(user)
                .build();

        createBookDTO = new CreateBookDTO(
                "Book Title", "123", LocalDate.of(2024, 1, 1),
                genre.getId(), BigDecimal.TEN, author.getId().toString());
    }

    @Test
    @DisplayName("Should return a book when ID exists")
    void shouldReturnBookById() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        Book result = bookService.show(bookId);
        assertEquals(book.getTitle(), result.getTitle());
    }

    @Test
    @DisplayName("Should throw exception when book not found")
    void shouldThrowWhenBookNotFound() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bookService.show(bookId));
    }

    @Test
    @DisplayName("Should return true if ISBN exists")
    void shouldCheckIsbnExistence() {
        when(bookRepository.existsByIsbn("123")).thenReturn(true);
        assertTrue(bookService.existsByIsbn("123"));
    }

    @Test
    @DisplayName("Should create a book successfully")
    void shouldCreateBook() throws MessagingException {
        when(bookRepository.existsByIsbn("123")).thenReturn(false);
        when(bookRepository.existsByTitle("Book Title")).thenReturn(false);
        when(authorService.show(author.getId())).thenReturn(author);
        when(userService.findById(user.getId())).thenReturn(user);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.create(createBookDTO, user);

        assertEquals("Book Title", result.getTitle());
        verify(emailService).send(eq("user@test.com"), eq("Book created!"), eq("mail/book-created"),
                any(BookEmailContextStrategy.class));
    }

    @Test
    @DisplayName("Should not create if ISBN exists")
    void shouldThrowIfIsbnExists() {
        when(bookRepository.existsByIsbn("123")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> bookService.create(createBookDTO, user));
    }

    @Test
    @DisplayName("Should update a book successfully")
    void shouldUpdateBook() throws MessagingException {
        Book updated = BookFactory.createFrom(book);
        updated.setAuthor(author);
        updated.setUser(user);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(authorService.show(author.getId())).thenReturn(author);
        when(userService.findById(user.getId())).thenReturn(user);
        when(bookRepository.existsByIsbn(any())).thenReturn(false);
        when(bookRepository.existsByTitle(any())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(updated);

        Book result = bookService.update(bookId, updated, user);

        assertEquals(book.getTitle(), result.getTitle());
        verify(emailService).send(eq("user@test.com"), any(), any(), any(BookEmailContextStrategy.class));
    }

    @Test
    @DisplayName("Should throw UnauthorizedException when updating not owned book")
    void shouldThrowUnauthorizedOnUpdate() {
        User anotherUser = new User();
        anotherUser.setId(UUID.randomUUID().toString());

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        assertThrows(UnauthorizedException.class, () -> bookService.update(bookId, book, anotherUser));
    }

    @Test
    @DisplayName("Should delete book successfully")
    void shouldDeleteBook() throws MessagingException {
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(userService.findById(user.getId())).thenReturn(user);

        bookService.delete(bookId, user);

        verify(bookRepository).delete(book);
        verify(emailService).send(eq("user@test.com"), any(), any(), any(BookEmailContextStrategy.class));
    }

    @Test
    @DisplayName("Should throw UnauthorizedException when deleting not owned book")
    void shouldThrowUnauthorizedOnDelete() {
        User anotherUser = new User();
        anotherUser.setId(UUID.randomUUID().toString());

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(userService.findById(anotherUser.getId())).thenReturn(anotherUser);

        assertThrows(UnauthorizedException.class, () -> bookService.delete(bookId, anotherUser));
    }

    @Test
    @DisplayName("Should upload file successfully")
    void shouldUploadBookFile() throws Exception {
        when(userService.findById(user.getId())).thenReturn(user);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(file.getOriginalFilename()).thenReturn("file.pdf");
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("data".getBytes()));
        when(file.getContentType()).thenReturn("application/pdf");
        when(storageService.generateFileName(any(), any())).thenReturn("file-123.pdf");
        when(storageService.getFileUrl(any(), any())).thenReturn("http://bucket/file-123.pdf");

        UploadFileDTO result = bookService.uploadBookFile(bookId, user, file);

        assertEquals("Upload successfully!", result.message());
        assertTrue(result.filePath().contains("http"));
    }

    @Test
    @DisplayName("Should return book list in index()")
    void shouldReturnBooksInIndex() {
        var pageable = Pageable.unpaged();
        var page = new PageImpl<>(List.of(book));

        // when(bookRepository.findAll(any(), eq(pageable))).thenReturn(page);

        Page<Book> result = bookService.index(pageable, "Title", "123", "Author", Genre.Fiction, "BR");

        assertEquals(1, result.getTotalElements());
    }
}
