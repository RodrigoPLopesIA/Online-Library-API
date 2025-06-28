package br.com.rodrigo.onlinelibraryapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.reactive.server.WebTestClient;
import br.com.rodrigo.onlinelibraryapi.dtos.CreateUserDto;
import br.com.rodrigo.onlinelibraryapi.dtos.ListUserDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/user-delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class UsersTest {

    @Autowired
    private WebTestClient testClient;

    @Test
    public void shouldCreateNewUserAndReturn201() {
        ListUserDto user = testClient.post().uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserDto(
                        "Rodrigo",
                        "Lopes",
                        "rodrigo@email.com",
                        "12341234",
                        "Rua das Flores",
                        "123",
                        "Apto 45",
                        "Centro",
                        "São Paulo",
                        "SP",
                        "01000-000"))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(ListUserDto.class)
                .returnResult()
                .getResponseBody();


        org.assertj.core.api.Assertions.assertThat(user).isNotNull();
        org.assertj.core.api.Assertions.assertThat(user.id()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(user.email()).isNotNull();


            
    }
}
