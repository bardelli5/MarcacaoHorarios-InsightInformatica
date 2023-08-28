package com.example.marcacaohorarios.app.service.impl;

import com.example.marcacaohorarios.app.dto.request.WorkHourRequest;
import com.example.marcacaohorarios.app.dto.response.WorkHourResponse;
import com.example.marcacaohorarios.app.service.WorkHourService;
import com.example.marcacaohorarios.domain.exception.InvalidWorkHourRequest;
import com.example.marcacaohorarios.domain.exception.WorkHourOversizeException;
import com.example.marcacaohorarios.domain.model.WorkHour;
import com.example.marcacaohorarios.domain.usecase.WorkHourUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class WorkHourServiceImpl implements WorkHourService {

    WorkHourUseCase useCase;

    @Inject
    public WorkHourServiceImpl(WorkHourUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public List<WorkHourResponse> newWorkHour(
            WorkHourRequest request
    ) throws WorkHourOversizeException, InvalidWorkHourRequest {
        useCase.checkForOversize();
        useCase.validateRequest(request);
        return useCase.newWorkHour(request);
    }

    @Override
    public List<WorkHourResponse> deleteWorkHour(WorkHourRequest request) {
        return useCase.deleteWorkHour(request);
    }

    @Override
    public List<WorkHour> getCreatedWorkHours() {
        return new ArrayList<>(useCase.getCreatedWorkHour());
    }
}
