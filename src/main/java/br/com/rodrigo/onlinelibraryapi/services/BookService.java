package br.com.rodrigo.onlinelibraryapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.rodrigo.onlinelibraryapi.dtos.CreateBookDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Book;
import br.com.rodrigo.onlinelibraryapi.repositories.BookRepository;

@Service
public class BookService {


    @Autowired
    private BookRepository bookRepository;


    public Book createBook(CreateBookDTO data) {
       
        // verify if book alread exists by isbn
        if (bookRepository.existsByIsbn(data.isbn())) {
            throw new IllegalArgumentException("Book with ISBN " + data.isbn() + " already exists.");
        }

        // verify if book already exists by title
        if (bookRepository.existsByTitle(data.title())) {
            throw new IllegalArgumentException("Book with title " + data.title() + " already exists.");
        }
        

        // verify if author exists by id



        return new Book();
    }
    
}
