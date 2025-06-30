package br.com.rodrigo.onlinelibraryapi.dtos.author;

import java.sql.Date;
import java.time.Instant;
import java.util.UUID;

import br.com.rodrigo.onlinelibraryapi.entities.Author;

public record ListAuthorDTO(UUID id, String name, Date dateBirth, String nationality, Instant createdAt,
        Instant updatedAt) {

    public ListAuthorDTO(Author author) {
        this(author.getId(), author.getName(), author.getDateBirth(), author.getNationality(), author.getCreatedAt(),
                author.getUpdatedAt());

    }

}
