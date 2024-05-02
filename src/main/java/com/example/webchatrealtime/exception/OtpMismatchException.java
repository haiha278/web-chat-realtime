package com.example.webchatrealtime.exception;

public class OtpMismatchException extends RuntimeException{
    public OtpMismatchException(String message) {
        super(message);
    }
}
