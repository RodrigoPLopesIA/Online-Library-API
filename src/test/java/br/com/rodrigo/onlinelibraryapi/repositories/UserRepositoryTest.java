package br.com.rodrigo.onlinelibraryapi.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import br.com.rodrigo.onlinelibraryapi.entities.User;
import br.com.rodrigo.onlinelibraryapi.integration.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTest extends AbstractIntegrationTest {
    

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
}
