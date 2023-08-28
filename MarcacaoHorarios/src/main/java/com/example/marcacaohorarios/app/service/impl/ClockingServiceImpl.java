package com.example.marcacaohorarios.app.service.impl;

import com.example.marcacaohorarios.app.dto.request.ClockingRequest;
import com.example.marcacaohorarios.app.dto.response.ClockingResponse;
import com.example.marcacaohorarios.app.service.ClockingService;
import com.example.marcacaohorarios.app.service.WorkHourService;
import com.example.marcacaohorarios.domain.exception.InvalidClockingException;
import com.example.marcacaohorarios.domain.model.WorkHour;
import com.example.marcacaohorarios.domain.usecase.ClockingUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ClockingServiceImpl implements ClockingService {

    WorkHourService workHourService;
    ClockingUseCase clockingUseCase;

    @Inject
    public ClockingServiceImpl(WorkHourService workHourService, ClockingUseCase clockingUseCase) {
        this.workHourService = workHourService;
        this.clockingUseCase = clockingUseCase;
    }

    @Override
    public List<ClockingResponse> newClocking(ClockingRequest request) throws InvalidClockingException {
        clockingUseCase.validateRequest(request);
        List<WorkHour> createdWorkHours = workHourService.getCreatedWorkHours();

        if (createdWorkHours.isEmpty()) {
            throw new InvalidClockingException("Não há Horarios de Trabalho registrados");
        }

        return clockingUseCase.newClocking(request, createdWorkHours);
    }
}