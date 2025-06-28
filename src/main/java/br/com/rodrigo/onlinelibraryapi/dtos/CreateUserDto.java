package br.com.rodrigo.onlinelibraryapi.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDto(
        @NotBlank(message = "The first name is required") String first_name,
        @NotBlank(message = "The last name is required") String last_name,
        @NotBlank(message = "The email is required") @Email(message = "The field email must be a valid email.") String email,
        @NotBlank(message = "The password is required") @Size(min = 6, max = 18, message = "THe password must be between 6 and 18 characters") String password,
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String zipCode) {
}
