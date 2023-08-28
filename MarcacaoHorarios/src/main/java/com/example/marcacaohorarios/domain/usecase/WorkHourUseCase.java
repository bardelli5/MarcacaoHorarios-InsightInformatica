package com.example.marcacaohorarios.domain.usecase;

import com.example.marcacaohorarios.app.dto.request.WorkHourRequest;
import com.example.marcacaohorarios.app.dto.response.WorkHourResponse;
import com.example.marcacaohorarios.domain.exception.InvalidWorkHourRequest;
import com.example.marcacaohorarios.domain.exception.WorkHourOversizeException;
import com.example.marcacaohorarios.domain.model.WorkHour;

import java.util.List;
import java.util.Set;

public interface WorkHourUseCase {
    void validateRequest(WorkHourRequest request) throws InvalidWorkHourRequest;

    void checkForOversize() throws WorkHourOversizeException;

    List<WorkHourResponse> newWorkHour(WorkHourRequest request);

    List<WorkHourResponse> deleteWorkHour(WorkHourRequest request);

    Set<WorkHour> getCreatedWorkHour();
}
