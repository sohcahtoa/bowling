package com.example.bowling.exception;

public class InvalidFrameException extends RuntimeException {
    public InvalidFrameException(String message) {
        super(message);
    }
}
