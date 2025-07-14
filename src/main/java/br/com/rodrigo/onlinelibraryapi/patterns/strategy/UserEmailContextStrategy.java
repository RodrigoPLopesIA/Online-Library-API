package br.com.rodrigo.onlinelibraryapi.patterns.strategy;

import org.thymeleaf.context.Context;

import br.com.rodrigo.onlinelibraryapi.entities.User;


public class UserEmailContextStrategy implements EmailContextStrategy {

    private final User user;

    public UserEmailContextStrategy(User user) {
        this.user = user;
    }

    @Override
    public Context buildContext() {
        Context context = new Context();
        context.setVariable("userName", user.getUsername());
        context.setVariable("profileUrl", "http://localhost:8080/users/" + user.getId());
        return context;
    }
}

