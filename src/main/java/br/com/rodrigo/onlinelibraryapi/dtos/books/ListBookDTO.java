package br.com.rodrigo.onlinelibraryapi.dtos.books;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import br.com.rodrigo.onlinelibraryapi.dtos.author.ListAuthorDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Book;
import br.com.rodrigo.onlinelibraryapi.enums.Genre;

public record ListBookDTO(UUID id, String title, String isbn, LocalDate publicationDate, Genre genre, BigDecimal price,
        ListAuthorDTO author, Instant createdAt, Instant updatedAt) {

    public ListBookDTO(Book book) {
        this(book.getId(), book.getTitle(), book.getIsbn(), book.getPublicationDate(), book.getGenre(), book.getPrice(),
                new ListAuthorDTO(book.getAuthor()), book.getCreatedAt(), book.getUpdatedAt());
    }
}
