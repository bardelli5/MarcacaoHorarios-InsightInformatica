package com.example.marcacaohorarios.domain.exception;

public class WorkHourOversizeException extends RuntimeException {
    public WorkHourOversizeException(String message) {
        super(message);
    }
}
