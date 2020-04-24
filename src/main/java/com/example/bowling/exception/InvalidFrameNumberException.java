package com.example.bowling.exception;

public class InvalidFrameNumberException extends RuntimeException {
    public InvalidFrameNumberException(String message) {
        super(message);
    }
}
