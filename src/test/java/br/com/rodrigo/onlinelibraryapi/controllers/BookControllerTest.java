package br.com.rodrigo.onlinelibraryapi.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.rodrigo.onlinelibraryapi.config.SpringSercurityConfig;
import br.com.rodrigo.onlinelibraryapi.dtos.books.CreateBookDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.books.ListBookDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Author;
import br.com.rodrigo.onlinelibraryapi.entities.Book;
import br.com.rodrigo.onlinelibraryapi.entities.Category;
import br.com.rodrigo.onlinelibraryapi.mapper.AuthorMapper;
import br.com.rodrigo.onlinelibraryapi.mapper.BookMapper;
import br.com.rodrigo.onlinelibraryapi.mapper.UserMapper;
import br.com.rodrigo.onlinelibraryapi.repositories.UserRepository;
import br.com.rodrigo.onlinelibraryapi.services.AuthenticationService;
import br.com.rodrigo.onlinelibraryapi.services.AuthorService;
import br.com.rodrigo.onlinelibraryapi.services.BookService;
import br.com.rodrigo.onlinelibraryapi.services.EmailService;
import br.com.rodrigo.onlinelibraryapi.services.JWTService;
import br.com.rodrigo.onlinelibraryapi.services.ProfileService;
import br.com.rodrigo.onlinelibraryapi.services.UserService;

@WebMvcTest(BookController.class)
@Import(SpringSercurityConfig.class)
public class BookControllerTest {

        @Autowired
        private MockMvc mvc;

        @MockitoBean
        private AuthenticationService authenticationService;

        @MockitoBean
        private AuthorService authorService;

        @MockitoBean
        private BookService bookService;

        @MockitoBean
        private AuthorMapper authorMapper;

        @MockitoBean
        private BookMapper bookMapper;

        @MockitoBean
        private EmailService emailService;

        @MockitoBean
        private ProfileService profileService;

        @MockitoBean
        private UserService userService;

        @MockitoBean
        private UserMapper userMapper;

        @MockitoBean
        private UserRepository userRepository;

        @MockitoBean
        private JWTService jwtService;

        @Test
        @WithMockUser
        @DisplayName("GET /api/v1/books -> should return list of books")
        public void shouldReturnListOfBooks() throws Exception {
                Author author = Author.builder().name("test").id(UUID.randomUUID()).nationality("test")
                                .dateBirth(java.util.Date.from(Instant.now())).build();
                ;
                List<Category> categories = Arrays.asList(
                                Category.builder().id("930892ea-c858-4aa6-9a01-0c1dd9e23772").name("test").build());
                var book = Book.builder()
                                .id(UUID.randomUUID())
                                .title("test")
                                .isbn("123456")
                                .categories(categories)
                                .author(author)
                                .build();

                var dto = new ListBookDTO(book);

                var page = new PageImpl<>(java.util.List.of(book));

                BDDMockito.given(bookService.index(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                                Mockito.any(), Mockito.any()))
                                .willReturn(page);

                BDDMockito.given(bookMapper.toDto(Mockito.any(Book.class))).willReturn(dto);

                mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/v1/books")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "testUser", roles = { "USER", "ADMIN" })
        @DisplayName("POST /api/v1/books -> should create a new book")
        public void shouldCreateANewBook() throws Exception {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                var data = new CreateBookDTO("Test", "test", LocalDate.of(2025, 05, 25),
                                "930892ea-c858-4aa6-9a01-0c1dd9e23772",
                                BigDecimal.valueOf(25),
                                "930892ea-c858-4aa6-9a01-0c1dd9e23771");
                var json = mapper.writeValueAsString(data);

                var author = Author.builder().id(UUID.fromString("930892ea-c858-4aa6-9a01-0c1dd9e23771")).name("asdasd")
                                .nationality("sadasd").build();

                List<Category> categories = Arrays.asList(
                                Category.builder().id("930892ea-c858-4aa6-9a01-0c1dd9e23772").name("test").build());

                var book = Book.builder()
                                .id(UUID.fromString("930892ea-c858-4aa6-9a01-0c1dd9e23771"))
                                .isbn("Test")
                                .title("test")
                                .categories(categories)
                                .publicationDate(LocalDate.of(2025, 05, 25))
                                .author(author).build();

                BDDMockito.given(bookService.create(any(CreateBookDTO.class), Mockito.any())).willReturn(book);

                var request = post("/api/v1/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON).content(json);

                mvc.perform(request).andExpect(status().isCreated());
        }

        @Test
        @WithMockUser
        @DisplayName("GET /api/v1/books/{id} -> should return book by id")
        public void shouldReturnBookById() throws Exception {
                var bookId = UUID.randomUUID();
                Author author = Author.builder().name("test").id(UUID.randomUUID()).nationality("test")
                                .dateBirth(java.util.Date.from(Instant.now())).build();

                List<Category> categories = Arrays.asList(
                                Category.builder().id("930892ea-c858-4aa6-9a01-0c1dd9e23772").name("test").build());

                var book = Book.builder().id(bookId).categories(categories).title("test").isbn("123").author(author)
                                .build();
                var dto = new ListBookDTO(book);

                BDDMockito.given(bookService.show(eq(bookId))).willReturn(book);
                BDDMockito.given(bookMapper.toDto(book)).willReturn(dto);

                mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/v1/books/" + bookId)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        @DisplayName("PUT /api/v1/books/{id} -> should update book")
        public void shouldUpdateBook() throws Exception {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                var data = new CreateBookDTO("Updated", "111111", LocalDate.of(2025, 05, 25),
                                "930892ea-c858-4aa6-9a01-0c1dd9e23771",
                                BigDecimal.valueOf(30),
                                "930892ea-c858-4aa6-9a01-0c1dd9e23772");
                Author author = Author.builder().name("test").id(UUID.randomUUID()).nationality("test")
                                .dateBirth(java.util.Date.from(Instant.now())).build();
                List<Category> categories = Arrays.asList(
                                Category.builder().id("930892ea-c858-4aa6-9a01-0c1dd9e23772").name("test").build());

                var book = Book.builder().id(UUID.randomUUID()).categories(categories).title("Updated").author(author)
                                .isbn("111111").build();
                var dto = new ListBookDTO(book);

                BDDMockito.given(bookMapper.toEntity(data)).willReturn(book);
                BDDMockito.given(bookService.update(eq(book.getId()), eq(book), Mockito.any())).willReturn(book);
                BDDMockito.given(bookMapper.toDto(book)).willReturn(dto);

                var json = mapper.writeValueAsString(data);

                mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .put("/api/v1/books/" + book.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        @DisplayName("DELETE /api/v1/books/{id} -> should delete book")
        public void shouldDeleteBook() throws Exception {
                var bookId = UUID.randomUUID();

                mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/v1/books/" + bookId))
                                .andExpect(status().isNoContent());

                Mockito.verify(bookService, Mockito.times(1)).delete(eq(bookId), Mockito.any());
        }

        @Test
        @WithMockUser
        @DisplayName("PATCH /api/v1/books/{id}/upload -> should upload book file")
        public void shouldUploadBookFile() throws Exception {
                var bookId = UUID.randomUUID();

                var file = new org.springframework.mock.web.MockMultipartFile(
                                "file",
                                "book.pdf",
                                MediaType.APPLICATION_PDF_VALUE,
                                "dummy content".getBytes());

                var uploadDto = new br.com.rodrigo.onlinelibraryapi.dtos.files.UploadFileDTO("book.pdf",
                                "http://bucket/book.pdf");

                BDDMockito.given(bookService.uploadBookFile(eq(bookId), Mockito.any(), Mockito.any()))
                                .willReturn(uploadDto);

                mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .multipart("/api/v1/books/" + bookId + "/upload")
                                .file(file)
                                .with(req -> {
                                        req.setMethod("PATCH");
                                        return req;
                                }))
                                .andExpect(status().isOk());
        }

}
