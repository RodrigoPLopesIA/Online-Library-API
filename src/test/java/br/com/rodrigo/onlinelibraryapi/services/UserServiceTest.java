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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.rodrigo.onlinelibraryapi.dtos.user.CreateUserDto;
import br.com.rodrigo.onlinelibraryapi.entities.User;
import br.com.rodrigo.onlinelibraryapi.entities.embedded.Authentication;
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

        Mockito.when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        var result = userService.save(userDto);

        Assertions.assertThat(result).isNotNull();
    }
}
