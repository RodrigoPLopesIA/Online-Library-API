package br.com.rodrigo.onlinelibraryapi.patterns.strategy;

import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;

public interface EmailContextStrategy {
    Context buildContext();
}