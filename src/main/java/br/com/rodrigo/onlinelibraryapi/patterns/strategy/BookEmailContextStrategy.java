package br.com.rodrigo.onlinelibraryapi.patterns.strategy;

import org.thymeleaf.context.Context;
import br.com.rodrigo.onlinelibraryapi.entities.Book;

public class BookEmailContextStrategy implements EmailContextStrategy {

    private final Book book;

    public BookEmailContextStrategy(Book book) {
        this.book = book;
    }

    @Override
    public Context buildContext() {
        Context context = new Context();
        context.setVariable("userName", book.getUser().getUsername());
        context.setVariable("bookTitle", book.getTitle());
        context.setVariable("libraryUrl", "http://localhost:8080/book/" + book.getId());
        return context;
    }
}
