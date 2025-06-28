package br.com.rodrigo.onlinelibraryapi.exceptions;

public class UniqueViolationException extends RuntimeException{
    

    public UniqueViolationException(String message){
        super(message);

    }
}
