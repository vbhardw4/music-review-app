package com.reactivespring.exceptions;

public class MusicInfoServerException extends RuntimeException{
    private String message;


    public MusicInfoServerException(String message) {
        super(message);
        this.message = message;
    }
}