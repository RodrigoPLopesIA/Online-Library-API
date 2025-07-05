package br.com.rodrigo.onlinelibraryapi.dtos.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CredentialsDTO(
@NotEmpty
@Email    
String email, 

@NotEmpty
@Size(min = 8, max = 16)
String password) {

}
