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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;

import org.hamcrest.Matchers;

import br.com.rodrigo.onlinelibraryapi.config.SpringSercurityConfig;
import br.com.rodrigo.onlinelibraryapi.dtos.category.CreateCategoryDTO;
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
import jakarta.persistence.EntityNotFoundException;

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

        Category category;

        CreateCategoryDTO createCategoryDTO;

        String params;

        String CATEGORY_URI;

        ListCategoryDTO listCategoryDTO;

        Page<ListCategoryDTO> categoryPage;

        String json;

        String categoryId;

        @BeforeEach
        public void setup() throws JsonProcessingException {
                category = Category.builder().name("HORROR").id(UUID.randomUUID().toString()).build();
                params = "?name=Horror";
                CATEGORY_URI = "/api/v1/categories";
                listCategoryDTO = new ListCategoryDTO(category);
                categoryPage = new PageImpl<>(List.of(listCategoryDTO), PageRequest.of(0, 100),
                                1);
                createCategoryDTO = new CreateCategoryDTO("HORROR");

                json = new ObjectMapper().writeValueAsString(createCategoryDTO);
                categoryId = "d6e4331d-dd66-49b4-9be0-e0372a8f5013";
        }

        @Test
        @WithMockUser(username = "userTest")
        @DisplayName("Should return all book categories")
        public void shouldReturnAllBookCategories() throws Exception {

                var request = get(CATEGORY_URI.concat(params))
                                .accept(MediaType.APPLICATION_JSON);

                BDDMockito.given(categoryService.index(Mockito.any(Pageable.class), Mockito.anyString()))
                                .willReturn(categoryPage);

                mvc.perform(request)
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content", Matchers.hasSize(1)));

                Mockito.verify(categoryService, times(1)).index(Mockito.any(Pageable.class), Mockito.anyString());

        }

        @Test
        @WithMockUser(username = "userTest")
        @DisplayName("Should create a new category")
        public void shouldCreateANewCategory() throws Exception {

                BDDMockito.given(categoryService.save(Mockito.any(CreateCategoryDTO.class)))
                                .willReturn(new ListCategoryDTO(UUID.randomUUID().toString(), "TEST"));

                var request = post(CATEGORY_URI)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json);

                mvc.perform(request)
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id", Matchers.notNullValue()))
                                .andExpect(jsonPath("$.name", Matchers.notNullValue()))
                                .andExpect(header().exists("Location"));

                Mockito.verify(categoryService, times(1)).save(Mockito.any(CreateCategoryDTO.class));
        }

        @Test
        @WithMockUser(username = "userTest")
        @DisplayName("Should return a throw error 422 when try to create a new category")
        public void shouldReturnAThrowError422WhenTryToCreateANewCategory() throws Exception {

                createCategoryDTO = new CreateCategoryDTO("");

                json = new ObjectMapper().writeValueAsString(createCategoryDTO);

                var request = post(CATEGORY_URI)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json);

                mvc.perform(request)
                                .andExpect(status().isUnprocessableEntity())
                                .andExpect(jsonPath("$.path").value(Matchers.any(String.class)))
                                .andExpect(jsonPath("$.message").value(Matchers.any(String.class)));

                Mockito.verify(categoryService, never()).save(Mockito.any(CreateCategoryDTO.class));
        }

        @Test
        @WithMockUser(username = "userTest")
        @DisplayName("Should return a 200 when try to update a new category")
        public void shouldUpdateCategory() throws Exception {

                BDDMockito.given(categoryService.update(categoryId, createCategoryDTO)).willReturn(listCategoryDTO);

                var request = put(CATEGORY_URI.concat("/".concat(categoryId)))
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json);

                mvc.perform(request).andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value(createCategoryDTO.name()))
                                .andExpect(jsonPath("$.id").exists());

                Mockito.verify(categoryService, times(1)).update(categoryId, createCategoryDTO);
        }

        @Test
        @WithMockUser(username = "userTest")
        @DisplayName("Should return a throw error 422 when try to update a new category")
        public void shouldReturn422ErrorWhenTryToUpdateCategory() throws Exception {

                createCategoryDTO = new CreateCategoryDTO("");
                json = new ObjectMapper().writeValueAsString(createCategoryDTO);

                var request = put(CATEGORY_URI.concat("/".concat(categoryId)))
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json);

                mvc.perform(request)
                                .andExpect(status().isUnprocessableEntity())
                                .andExpect(jsonPath("$.path").value(Matchers.any(String.class)))
                                .andExpect(jsonPath("$.message").value(Matchers.any(String.class)));

                Mockito.verify(categoryService, never()).update(categoryId, createCategoryDTO);
        }

        @Test
        @WithMockUser(username = "userTest")
        @DisplayName("should return 404 when updating non-existent category")
        public void shouldReturn404WhenTryToUpdateCategory() throws Exception {

                BDDMockito.given(categoryService.update(categoryId, createCategoryDTO))
                                .willThrow(new EntityNotFoundException(
                                                String.format("Category %s not found!", categoryId)));

                var request = put(CATEGORY_URI.concat("/".concat(categoryId)))
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json);

                mvc.perform(request)
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.path").value(Matchers.any(String.class)))
                                .andExpect(jsonPath("$.message").value(Matchers.any(String.class))); // ← Corrigido aqui

                Mockito.verify(categoryService, times(1)).update(categoryId, createCategoryDTO);
        }

        @Test
        @WithMockUser(username = "userTest")
        @DisplayName("should return 409 when updating with an existing category name")
        public void shouldReturn409WhenCategoryAlreadyExists() throws Exception {
                BDDMockito.given(categoryService.update(categoryId, createCategoryDTO))
                                .willThrow(new IllegalArgumentException(String.format("Category %s already exists!",
                                                createCategoryDTO.name())));

                var request = put(CATEGORY_URI + "/" + categoryId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json);

                mvc.perform(request)
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value(Matchers.any(String.class)))
                                .andExpect(jsonPath("$.path").value(Matchers.any(String.class)));

                Mockito.verify(categoryService, times(1)).update(categoryId, createCategoryDTO);
        }

        @Test
        @DisplayName("should return a category by category id")
        @WithMockUser(username = "userTest")
        public void shouldReturnCategoryByCategoryId() throws Exception {

                BDDMockito.given(categoryService.show(Mockito.anyString())).willReturn(listCategoryDTO);
                var request = get(CATEGORY_URI + "/" + categoryId)
                                .accept(MediaType.APPLICATION_JSON);

                mvc.perform(request).andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").isNotEmpty())
                                .andExpect(jsonPath("$.name").isNotEmpty());

                Mockito.verify(categoryService, times(1)).show(categoryId);
        }

        @Test
        @DisplayName("should return error 404 when try to find a category by category id")
        @WithMockUser(username = "userTest")
        public void shouldReturn404ErrorWhenTryToFindCategoryByCategoryId() throws Exception {

                BDDMockito.given(categoryService.show(Mockito.anyString()))
                                .willThrow(new EntityNotFoundException(
                                                String.format("Category with ID %s not found.", categoryId)));

                var request = get(CATEGORY_URI + "/" + categoryId)
                                .accept(MediaType.APPLICATION_JSON);

                mvc.perform(request).andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.path").isNotEmpty())
                                .andExpect(jsonPath("$.message").isNotEmpty())
                                .andExpect(jsonPath("$.message")
                                                .value(String.format("Category with ID %s not found.", categoryId)));

                Mockito.verify(categoryService, times(1)).show(categoryId);
        }

        @Test
        @DisplayName("should return a category by category id")
        @WithMockUser(username = "userTest")
        public void shouldDeleteCategoryByIdSuccessfully() throws Exception {

                var request = delete(CATEGORY_URI + "/" + categoryId)
                                .accept(MediaType.APPLICATION_JSON);

                // Act + Assert
                mvc.perform(request)
                                .andExpect(status().isNoContent()); 

                verify(categoryService, times(1)).delete(categoryId);
        }

        @Test
        @DisplayName("should return error 404 when try to find a category by category id")
        @WithMockUser(username = "userTest")
        public void shouldReturn404ErrorWhenTryToDeleteCategoryByCategoryId() throws Exception {



                doThrow(new EntityNotFoundException(
                                String.format("Category with ID %s not found.", categoryId))).when(categoryService)
                                .delete(categoryId);

                var request = delete(CATEGORY_URI + "/" + categoryId)
                                .accept(MediaType.APPLICATION_JSON);

                mvc.perform(request).andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.path").isNotEmpty())
                                .andExpect(jsonPath("$.message").isNotEmpty())
                                .andExpect(jsonPath("$.message")
                                                .value(String.format("Category with ID %s not found.", categoryId)));

                Mockito.verify(categoryService, times(1)).delete(categoryId);
        }

}
