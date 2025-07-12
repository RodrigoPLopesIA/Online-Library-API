package br.com.rodrigo.onlinelibraryapi.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.rodrigo.onlinelibraryapi.config.SpringSercurityConfig;
import br.com.rodrigo.onlinelibraryapi.dtos.books.CreateBookDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Author;
import br.com.rodrigo.onlinelibraryapi.entities.Book;
import br.com.rodrigo.onlinelibraryapi.enums.Genre;
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

@WebMvcTest
@Import(SpringSercurityConfig.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorMapper authorMapper;

    @MockBean
    private BookMapper bookMapper;

    @MockBean
    private EmailService emailService;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JWTService jwtService;


    @Test
    @WithMockUser(username = "testUser", roles = { "USER", "ADMIN" })
    @DisplayName("POST /api/v1/books -> should create a new book")
    public void shouldCreateANewBook() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        var data = new CreateBookDTO("Test", "test", LocalDate.of(2025, 05, 25), Genre.Horror, BigDecimal.valueOf(25),
                UUID.fromString("930892ea-c858-4aa6-9a01-0c1dd9e23771"));
        var json = mapper.writeValueAsString(data);

        var author = Author.builder().id(UUID.fromString("930892ea-c858-4aa6-9a01-0c1dd9e23771")).name("asdasd")
                .nationality("sadasd").build();
        var book = Book.builder()
                .id(UUID.fromString("930892ea-c858-4aa6-9a01-0c1dd9e23771"))
                .isbn("Test")
                .title("test")
                .publicationDate(LocalDate.of(2025, 05, 25))
                .author(author).build();

        BDDMockito.given(bookService.create(any(CreateBookDTO.class), Mockito.any())).willReturn(book);


        var request = post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(json);

        mvc.perform(request).andExpect(status().isCreated());
    }

}
