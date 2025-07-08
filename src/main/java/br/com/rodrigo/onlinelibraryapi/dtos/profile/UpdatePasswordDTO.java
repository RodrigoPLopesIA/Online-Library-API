package br.com.rodrigo.onlinelibraryapi.dtos.profile;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordDTO(
        @NotBlank(message = "The current password is required") String currentPassword,
        @NotBlank(message = "The new password is required") String newPassword,
        @NotBlank(message = "The confirm password is required") String confirmPassword) {

}
