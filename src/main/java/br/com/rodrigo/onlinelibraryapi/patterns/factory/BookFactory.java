package br.com.rodrigo.onlinelibraryapi.patterns.factory;

import br.com.rodrigo.onlinelibraryapi.dtos.books.CreateBookDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Book;

public class BookFactory {

    public static Book createFrom(Book data) {
        return Book.builder()
                .id(data.getId())
                .isbn(data.getIsbn())
                .title(data.getTitle())
                .publicationDate(data.getPublicationDate())
                .price(data.getPrice()).build();
    }

    public static Book createFrom(CreateBookDTO data) {
        return Book.builder()
                .isbn(data.isbn())
                .title(data.title())
                .publicationDate(data.publicationDate())
                .price(data.price())
                .build();
    }
}
