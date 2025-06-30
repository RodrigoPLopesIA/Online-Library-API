package br.com.rodrigo.onlinelibraryapi.dtos.books;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import br.com.rodrigo.onlinelibraryapi.enums.Genre;

public record CreateBookDTO(
    @NotBlank
    @Size(max = 20)
    String isbn,

    @NotBlank
    @Size(max = 150)
    String title,

    @NotNull
    LocalDate publicationDate,

    @NotNull
    Genre genre,

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 13, fraction = 2)
    BigDecimal price,

    @NotNull
    UUID authorId
) {


}
