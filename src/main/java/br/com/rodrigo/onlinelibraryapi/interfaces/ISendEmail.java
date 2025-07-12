package br.com.rodrigo.onlinelibraryapi.interfaces;

import org.thymeleaf.context.IContext;

import jakarta.mail.MessagingException;

public interface ISendEmail {

    public void send(String to, String subject, String templateName, IContext context) throws MessagingException;

}
