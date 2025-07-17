package br.com.rodrigo.onlinelibraryapi.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;

import org.hamcrest.Matchers;

import br.com.rodrigo.onlinelibraryapi.config.SpringSercurityConfig;
import br.com.rodrigo.onlinelibraryapi.dtos.category.ListCategoryDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Category;
import br.com.rodrigo.onlinelibraryapi.mapper.AuthorMapper;
import br.com.rodrigo.onlinelibraryapi.mapper.BookMapper;
import br.com.rodrigo.onlinelibraryapi.mapper.UserMapper;
import br.com.rodrigo.onlinelibraryapi.repositories.UserRepository;
import br.com.rodrigo.onlinelibraryapi.services.AuthenticationService;
import br.com.rodrigo.onlinelibraryapi.services.AuthorService;
import br.com.rodrigo.onlinelibraryapi.services.BookService;
import br.com.rodrigo.onlinelibraryapi.services.CategoryService;
import br.com.rodrigo.onlinelibraryapi.services.JWTService;
import br.com.rodrigo.onlinelibraryapi.services.ProfileService;
import br.com.rodrigo.onlinelibraryapi.services.UserService;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest
@AutoConfigureMockMvc
@Import(SpringSercurityConfig.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoryService categoryService;
    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthorMapper authorMapper;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookMapper bookMapper;
    @MockBean
    private ProfileService profileService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JWTService jwtService;

    @Test
    @WithMockUser(username = "userTest")
    @DisplayName("Should return all book categories")
    public void shouldReturnAllBookCategories() throws Exception {

        var request = get("/api/v1/categories")
                .accept(MediaType.APPLICATION_JSON);

        Category category = Category.builder().name("HORROR").id(UUID.randomUUID().toString()).build();
        ListCategoryDTO listCategoryDTO = new ListCategoryDTO(category);
        
        Page<ListCategoryDTO> categoryPage = new PageImpl<>(List.of(listCategoryDTO), PageRequest.of(0, 100), 1);

        BDDMockito.given(categoryService.index(Mockito.any(Pageable.class))).willReturn(categoryPage);

        mvc.perform(request).andExpect(status().isOk());
        mvc.perform(request).andExpect(jsonPath("$.content", Matchers.hasSize(0)));
    }
}
