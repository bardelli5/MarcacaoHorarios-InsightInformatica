package com.example.marcacaohorarios.domain.exception;

public class InvalidWorkHourRequest extends RuntimeException {
    public InvalidWorkHourRequest(String message) {
        super(message);
    }
}
