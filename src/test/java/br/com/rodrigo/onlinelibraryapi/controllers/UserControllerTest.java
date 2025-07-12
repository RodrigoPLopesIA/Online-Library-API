package br.com.rodrigo.onlinelibraryapi.controllers;

import br.com.rodrigo.onlinelibraryapi.config.SpringSercurityConfig;
import br.com.rodrigo.onlinelibraryapi.dtos.user.CreateUserDto;
import br.com.rodrigo.onlinelibraryapi.dtos.user.ListUserDto;
import br.com.rodrigo.onlinelibraryapi.entities.User;
import br.com.rodrigo.onlinelibraryapi.mapper.UserMapper;
import br.com.rodrigo.onlinelibraryapi.repositories.UserRepository;
import br.com.rodrigo.onlinelibraryapi.services.JWTService;
import br.com.rodrigo.onlinelibraryapi.services.UserService;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@Import(SpringSercurityConfig.class)
@Slf4j
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JWTService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/users - should create a new user")
    public void shouldCreateNewUser() throws Exception {
        // Arrange
        CreateUserDto data = new CreateUserDto(
                "Rodrigo",
                "Lopes",
                "rodrigo@email.com",
                "12341234",
                "12341234",
                "google",
                "rua inga",
                "4",
                "casa 1",
                "carmari",
                "Nova iguaçu",
                "RJ",
                "26023140");

        User user = new User(data);
        user.setId("123456123456");
        ListUserDto listUserDto = new ListUserDto(user);

        given(userMapper.toUser(any(CreateUserDto.class))).willReturn(user);
        given(userMapper.toListUserDTO(any(User.class))).willReturn(listUserDto);
        given(userService.save(any(CreateUserDto.class))).willReturn(user);

        MockHttpServletRequestBuilder content = post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));
        // Act & Assert

        mvc.perform(content).andExpect(status().isCreated())
                .andExpect(jsonPath("first_name").value("Rodrigo"));

    }

    @Test
    @DisplayName("Should return invalid argument exception")
    public void shouldReturnInvalidArgumentException() throws Exception {

        CreateUserDto data = new CreateUserDto(
                null,
                null,
                null,
                null,
                null,
                "google",
                "rua inga",
                "4",
                "casa 1",
                "carmari",
                "Nova iguaçu",
                "RJ",
                "26023140");

        User user = new User(data);
        user.setId("123456123456");
        ListUserDto listUserDto = new ListUserDto(user);


        MockHttpServletRequestBuilder request = post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));

        

        mvc.perform(request).andExpect(status().isUnprocessableEntity());
        mvc.perform(request).andExpect(jsonPath("$.path").value(Matchers.any(String.class)));
        mvc.perform(request).andExpect(jsonPath("$.message").value(Matchers.any(String.class)));

    }

}