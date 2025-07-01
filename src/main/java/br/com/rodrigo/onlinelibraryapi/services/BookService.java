package br.com.rodrigo.onlinelibraryapi.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.rodrigo.onlinelibraryapi.dtos.books.CreateBookDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.books.ListBookDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Author;
import br.com.rodrigo.onlinelibraryapi.entities.Book;
import br.com.rodrigo.onlinelibraryapi.repositories.BookRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class BookService {


    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorService authorService;

    public Page<ListBookDTO> index(Pageable pageable) {
        return bookRepository.findAll(pageable).map(ListBookDTO::new);
    }

    public Book create(CreateBookDTO data) {
       
        // verify if book alread exists by isbn
        if (this.existsByIsbn(data.isbn())) {
            throw new IllegalArgumentException("Book with ISBN " + data.isbn() + " already exists.");
        }

        // verify if book already exists by title
        if (this.existsByTitle(data.title())) {
            throw new IllegalArgumentException("Book with title " + data.title() + " already exists.");
        }

        // verify if author exists by id
        Author author = authorService.show(data.authorId());


        Book book = new Book(data);
        book.setAuthor(author);

        return bookRepository.save(book);
    }

    public Book update(UUID id, CreateBookDTO data) {
        Book book = this.show(id);

        // verify if book already exists by isbn
        if (!book.getIsbn().equals(data.isbn()) && this.existsByIsbn(data.isbn())) {
            throw new IllegalArgumentException("Book with ISBN " + data.isbn() + " already exists.");
        }

        // verify if book already exists by title
        if (!book.getTitle().equals(data.title()) && this.existsByTitle(data.title())) {
            throw new IllegalArgumentException("Book with title " + data.title() + " already exists.");
        }

        // verify if author exists by id
        Author author = authorService.show(data.authorId());

        book.update(data);
        book.setAuthor(author);

        return bookRepository.save(book);
    }
    
    public Book show(UUID id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with ID " + id + " not found."));
    }
    
    public Boolean existsByIsbn(String isbn) {
        return bookRepository.existsByIsbn(isbn);
    }
    public Boolean existsByTitle(String title) {
        return bookRepository.existsByTitle(title);
    }
    
    
}
