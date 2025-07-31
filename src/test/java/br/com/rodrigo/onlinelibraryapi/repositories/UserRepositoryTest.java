package br.com.rodrigo.onlinelibraryapi.repositories;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import br.com.rodrigo.onlinelibraryapi.entities.User;

@DataJpaTest
public class UserRepositoryTest {
    

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("shoud create a new user")
    public void shouldCreateUser(){
        User user = new User();
        this.userRepository.save(user);

        Assertions.assertThat(user).isNotNull();
    }

    @Test
    @DisplayName("Should find all products")
    public void shouldReturnAllUsers(){
        List<User> users =this.userRepository.findAll();

        Assertions.assertThat(users).isNotNull();
    }
}
