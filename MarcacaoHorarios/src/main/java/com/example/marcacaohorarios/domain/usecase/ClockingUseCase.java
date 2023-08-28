package com.example.marcacaohorarios.domain.usecase;

import com.example.marcacaohorarios.app.dto.request.ClockingRequest;
import com.example.marcacaohorarios.app.dto.response.ClockingResponse;
import com.example.marcacaohorarios.domain.exception.InvalidClockingException;
import com.example.marcacaohorarios.domain.model.WorkHour;

import java.util.List;

public interface ClockingUseCase {
    void validateRequest(ClockingRequest request) throws InvalidClockingException;
    List<ClockingResponse> newClocking(ClockingRequest request, List<WorkHour> createdWorkHour);
}
