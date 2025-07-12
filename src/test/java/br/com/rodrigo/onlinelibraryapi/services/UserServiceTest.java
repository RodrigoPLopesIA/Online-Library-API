package br.com.rodrigo.onlinelibraryapi.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.rodrigo.onlinelibraryapi.dtos.user.CreateUserDto;
import br.com.rodrigo.onlinelibraryapi.entities.User;
import br.com.rodrigo.onlinelibraryapi.entities.embedded.Authentication;
import br.com.rodrigo.onlinelibraryapi.entities.embedded.Name;
import br.com.rodrigo.onlinelibraryapi.exceptions.UniqueViolationException;
import br.com.rodrigo.onlinelibraryapi.mapper.UserMapper;
import br.com.rodrigo.onlinelibraryapi.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    CreateUserDto userDto;

    @BeforeEach
    public void setupe() {
        userDto = new CreateUserDto(
                "Rodrigo",
                "Lopes",
                "rodrigo@email.com",
                "12345678",
                "12345678",
                "google",
                "Rua das Flores",
                "123",
                "Apto 202",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000");
    }

    @Test
    @DisplayName("User Service -> should create a new user")
    public void shouldCreateNewUser() {
        User user = new User();
        Mockito.when(userMapper.toUser(userDto)).thenReturn(user);
        user.setAuthentication(new Authentication());
        user.setName(new Name(userDto.first_name(), userDto.last_name()));

        Mockito.when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        var result = userService.save(userDto);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getName().getFirst_name()).isEqualTo("Rodrigo");

    }

    @Test
    @DisplayName("User Service -> should throw exception when password and confirm password do not match")
    public void shouldThrowExceptionWhenPasswordsDoNotMatch() {

        var dto = new CreateUserDto(
                "Rodrigo",
                "Lopes",
                "rodrigo@email.com",
                "12345678",
                "123456789",
                "google",
                "Rua das Flores",
                "123",
                "Apto 202",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000");
        User user = new User();
        Mockito.when(userMapper.toUser(dto)).thenReturn(user);
        user.setAuthentication(new Authentication());
        user.setName(new Name(dto.first_name(), dto.last_name()));

        var result = Assertions.catchThrowable(() -> userService.save(dto));

        Assertions.assertThat(result).isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThat(result.getMessage()).isEqualTo("Password and confirm password must be equal.");
    }

    @Test
    @DisplayName("User Service -> should throw exception when email is already registered")
    public void shouldThrowExceptionWhenEmailIsAlreadyRegistered() {

        var dto = new CreateUserDto(
                "Rodrigo",
                "Lopes",
                "rodrigo@email.com",
                "12345678",
                "12345678",
                "google",
                "Rua das Flores",
                "123",
                "Apto 202",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000");
        User user = new User();
        Mockito.when(userMapper.toUser(dto)).thenReturn(user);
        user.setAuthentication(new Authentication());
        user.setName(new Name(dto.first_name(), dto.last_name()));
        Mockito.when(userRepository.save(user))
                .thenThrow(new UniqueViolationException(String.format("user %s already registered", dto.email())));

        var result = Assertions.catchThrowable(() -> userService.save(dto));
        Assertions.assertThat(result).isInstanceOf(UniqueViolationException.class);
        Assertions.assertThat(result.getMessage()).isEqualTo("user rodrigo@email.com already registered");
    }
}
