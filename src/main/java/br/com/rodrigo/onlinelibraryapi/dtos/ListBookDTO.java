package br.com.rodrigo.onlinelibraryapi.dtos;

import br.com.rodrigo.onlinelibraryapi.entities.Book;

public record ListBookDTO(String title, String isbn) {
    

    public ListBookDTO(Book book){
        this(book.getTitle(), book.getIsbn());
    }
}
