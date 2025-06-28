package br.com.rodrigo.onlinelibraryapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SpringDocOpenAI {
    

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI().info(new Info()
            .title("Online Library API")
            .description("This API provides endpoints to manage books, authors, and users in an online library system.")
            .version("V1").contact(new Contact().email("rodrigo@email.com"))
        );
    }
}
