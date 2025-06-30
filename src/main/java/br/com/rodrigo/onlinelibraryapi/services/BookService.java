package br.com.rodrigo.onlinelibraryapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.rodrigo.onlinelibraryapi.dtos.CreateBookDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Author;
import br.com.rodrigo.onlinelibraryapi.entities.Book;
import br.com.rodrigo.onlinelibraryapi.repositories.BookRepository;

@Service
public class BookService {


    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorService authorService;

    public Book create(CreateBookDTO data) {
       
        // verify if book alread exists by isbn
        if (bookRepository.existsByIsbn(data.isbn())) {
            throw new IllegalArgumentException("Book with ISBN " + data.isbn() + " already exists.");
        }

        // verify if book already exists by title
        if (bookRepository.existsByTitle(data.title())) {
            throw new IllegalArgumentException("Book with title " + data.title() + " already exists.");
        }

        // verify if author exists by id
        Author author = authorService.show(data.authorId());


        Book book = new Book(data);
        book.setAuthor(author);

        return bookRepository.save(book);
    }
    
}
