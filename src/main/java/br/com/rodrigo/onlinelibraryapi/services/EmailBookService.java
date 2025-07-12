package br.com.rodrigo.onlinelibraryapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import jakarta.mail.MessagingException;

@Service
public class EmailBookService{

    @Autowired
    private EmailService emailService;
    
    public void send(String to, String subject) throws MessagingException {
        try {
            Context context = new Context();
    
            context.setVariable("userName", "test");
            context.setVariable("bookTitle", "test");
            context.setVariable("libraryUrl", "test");
            this.emailService.send(to, subject, "mail/book-created", context);
            
        } catch (MessagingException e) {
            throw new MessagingException(String.format("Error to send email: %s", e.getMessage()));
        }
    }
    

}   
