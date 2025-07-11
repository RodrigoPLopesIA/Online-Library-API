package br.com.rodrigo.onlinelibraryapi.controllers;

import br.com.rodrigo.onlinelibraryapi.config.SpringSercurityConfig;
import br.com.rodrigo.onlinelibraryapi.dtos.user.CreateUserDto;
import br.com.rodrigo.onlinelibraryapi.dtos.user.ListUserDto;
import br.com.rodrigo.onlinelibraryapi.entities.User;
import br.com.rodrigo.onlinelibraryapi.filters.AuthenticationFilter;
import br.com.rodrigo.onlinelibraryapi.mapper.UserMapper;
import br.com.rodrigo.onlinelibraryapi.services.JWTService;
import br.com.rodrigo.onlinelibraryapi.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@Import(SpringSercurityConfig.class)
public class UserControllerTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private AuthenticationFilter authenticationFilter;

    @MockBean
    private JWTService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/users - Deve criar um novo usuário com sucesso")
    public void shouldCreateNewUser() throws Exception {
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
        ListUserDto listUserDto = new ListUserDto(user);

        given(userMapper.toUser(any(CreateUserDto.class))).willReturn(user);
        given(userMapper.toListUserDTO(any(User.class))).willReturn(listUserDto);
        given(userService.save(any(CreateUserDto.class))).willReturn(user);

        // Act & Assert
        mvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }
}