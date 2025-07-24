package br.com.rodrigo.onlinelibraryapi.controllers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import static io.restassured.RestAssured.given;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.rodrigo.onlinelibraryapi.config.TestConfigs;
import br.com.rodrigo.onlinelibraryapi.dtos.user.CreateUserDto;
import br.com.rodrigo.onlinelibraryapi.dtos.user.ListUserDto;
import br.com.rodrigo.onlinelibraryapi.integration.AbstractIntegrationTest;
import br.com.rodrigo.onlinelibraryapi.services.UserService;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import com.github.javafaker.Faker;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class UserControllerIntegrationTest extends AbstractIntegrationTest {

    static RequestSpecification requestSpecification;

    static ObjectMapper objectMapper;

    static UserService userService;
    static CreateUserDto data;
    static ListUserDto listData;
    static Faker fake;

    @BeforeAll()
    public static void setup() throws JsonProcessingException {
        fake = new Faker();
        data = new CreateUserDto(
                "Rodrigo",
                "Lopes",
                fake.internet().emailAddress(),
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

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        requestSpecification = new RequestSpecBuilder()
                .setBasePath("/api/v1/users")
                .setPort(TestConfigs.PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
        ;

    }

    @Test
    @DisplayName("should create a new user")
    public void shouldCreateNewUser() throws JsonMappingException, JsonProcessingException {

        String content = given()
                .spec(requestSpecification)
                .contentType(TestConfigs.CONTENT_TYPE)
                .body(objectMapper.writeValueAsString(data))
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body().asString();

        var listData = objectMapper.readValue(content, ListUserDto.class);

        Assertions.assertThat(listData).isNotNull();
        Assertions.assertThat(listData.email()).isEqualTo(data.email());
        Assertions.assertThat(listData.first_name()).isEqualTo(data.first_name());
    }
}
