package br.com.rodrigo.onlinelibraryapi.dtos.books;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import br.com.rodrigo.onlinelibraryapi.dtos.author.ListAuthorDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.category.ListCategoryDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Book;
import br.com.rodrigo.onlinelibraryapi.enums.Genre;

public record ListBookDTO(UUID id, String title, String isbn, LocalDate publicationDate, List<ListCategoryDTO> genres,
        BigDecimal price,
        ListAuthorDTO author, String bookFile, Instant createdAt, Instant updatedAt) {

    public ListBookDTO(Book book) {
        this(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getPublicationDate(),
                book.getCategories().stream().map(ListCategoryDTO::new).toList(),
                book.getPrice(),
                new ListAuthorDTO(book.getAuthor()), 
                book.getBookFile(),
                book.getCreatedAt(),
                book.getUpdatedAt());
    }
}
