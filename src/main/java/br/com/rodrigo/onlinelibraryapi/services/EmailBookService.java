package br.com.rodrigo.onlinelibraryapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import br.com.rodrigo.onlinelibraryapi.entities.Book;
import jakarta.mail.MessagingException;

@Service
public class EmailBookService {

    @Autowired
    private EmailService emailService;

    public void send(String to, String subject, String template, Book book) throws MessagingException {
        try {
            Context context = new Context();

            context.setVariable("userName", book.getUser().getUsername());
            context.setVariable("bookTitle", book.getTitle());
            context.setVariable("libraryUrl", "http://localhost:8080/book/" + book.getId());
            this.emailService.send(book.getUser().getUsername(), subject, "mail/book-" + template, context);

        } catch (MessagingException e) {
            throw new MessagingException(String.format("Error to send email: %s", e.getMessage()));
        }
    }

}
