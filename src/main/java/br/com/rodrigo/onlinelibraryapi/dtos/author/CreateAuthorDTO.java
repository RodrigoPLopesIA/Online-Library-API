package br.com.rodrigo.onlinelibraryapi.dtos.author;

import java.sql.Date;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record CreateAuthorDTO(
        @NotEmpty @Size(max = 100, min = 1) String name,
        @NotEmpty @Past Date dateBirth,
        @NotEmpty @Size(max = 50, min = 1) String nationality) {

}
