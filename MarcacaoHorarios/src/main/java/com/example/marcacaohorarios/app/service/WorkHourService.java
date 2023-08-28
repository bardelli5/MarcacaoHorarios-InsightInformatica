package com.example.marcacaohorarios.app.service;

import com.example.marcacaohorarios.app.dto.request.WorkHourRequest;
import com.example.marcacaohorarios.app.dto.response.WorkHourResponse;
import com.example.marcacaohorarios.domain.exception.InvalidWorkHourRequest;
import com.example.marcacaohorarios.domain.exception.WorkHourOversizeException;
import com.example.marcacaohorarios.domain.model.WorkHour;

import java.util.List;

public interface WorkHourService {
    List<WorkHourResponse> newWorkHour(WorkHourRequest request)
            throws WorkHourOversizeException, InvalidWorkHourRequest;
    List<WorkHourResponse> deleteWorkHour(WorkHourRequest request);
    List<WorkHour> getCreatedWorkHours();
}
