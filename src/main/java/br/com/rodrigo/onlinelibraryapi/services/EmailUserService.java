package br.com.rodrigo.onlinelibraryapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import br.com.rodrigo.onlinelibraryapi.entities.Book;
import br.com.rodrigo.onlinelibraryapi.entities.User;
import jakarta.mail.MessagingException;

@Service
public class EmailUserService {

    @Autowired
    private EmailService emailService;

    public void send(String to, String subject, String template, User user) throws MessagingException {
        try {
            Context context = new Context();

            context.setVariable("userName", user.getUsername());
            context.setVariable("profileUrl", "http://localhost:8080/users/" + user.getId());
            this.emailService.send(user.getUsername(), subject, "mail/" + template, context);

        } catch (MessagingException e) {
            throw new MessagingException(String.format("Error to send email: %s", e.getMessage()));
        }
    }

}
