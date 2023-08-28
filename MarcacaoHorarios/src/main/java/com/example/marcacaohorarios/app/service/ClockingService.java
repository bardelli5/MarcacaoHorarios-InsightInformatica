package com.example.marcacaohorarios.app.service;

import com.example.marcacaohorarios.app.dto.request.ClockingRequest;
import com.example.marcacaohorarios.app.dto.response.ClockingResponse;
import com.example.marcacaohorarios.domain.exception.InvalidClockingException;

import java.util.List;

public interface ClockingService {
    List<ClockingResponse> newClocking(ClockingRequest request)
            throws InvalidClockingException;
}
