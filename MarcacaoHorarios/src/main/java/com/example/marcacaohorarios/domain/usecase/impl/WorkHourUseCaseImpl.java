package com.example.marcacaohorarios.domain.usecase.impl;

import com.example.marcacaohorarios.app.dto.request.WorkHourRequest;
import com.example.marcacaohorarios.app.dto.response.WorkHourResponse;
import com.example.marcacaohorarios.cross.utils.ValidatorUtils;
import com.example.marcacaohorarios.domain.exception.InvalidWorkHourRequest;
import com.example.marcacaohorarios.domain.exception.WorkHourOversizeException;
import com.example.marcacaohorarios.domain.model.WorkHour;
import com.example.marcacaohorarios.domain.usecase.WorkHourUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class WorkHourUseCaseImpl implements WorkHourUseCase {
    Set<WorkHour> createdWorkHour;

    public WorkHourUseCaseImpl() {
        this.createdWorkHour = new HashSet<>();
    }

    @Override
    public void validateRequest(WorkHourRequest request) throws InvalidWorkHourRequest {
        Set<ConstraintViolation<WorkHourRequest>> constraints = ValidatorUtils.validate(request);

        if (!constraints.isEmpty()) {
            String message = constraints.stream().findFirst().get().getMessage();
            throw new InvalidWorkHourRequest(message);
        }
    }

    @Override
    public void checkForOversize() throws WorkHourOversizeException {
        if (this.createdWorkHour.size() >= 3)
            throw new WorkHourOversizeException("Horários de Trabalho excedem 3 itens");
    }

    @Override
    public List<WorkHourResponse> newWorkHour(WorkHourRequest request) {
        createdWorkHour.add(
                WorkHour.builder()
                        .entry(request.getEntry())
                        .departure(request.getDeparture())
                        .build()
        );

        return mapToResponse();
    }

    @Override
    public List<WorkHourResponse> deleteWorkHour(WorkHourRequest request) {
        WorkHour wh = WorkHour.builder().entry(request.getEntry()).departure(request.getDeparture()).build();

        createdWorkHour.remove(wh);
        return mapToResponse();
    }

    @Override
    public Set<WorkHour> getCreatedWorkHour() {
        return createdWorkHour;
    }

    private List<WorkHourResponse> mapToResponse() {
        return createdWorkHour.stream().map(
                wh -> WorkHourResponse.builder()
                        .entry(wh.getEntry())
                        .departure(wh.getDeparture())
                        .build()
        ).collect(Collectors.toList());
    }
}
