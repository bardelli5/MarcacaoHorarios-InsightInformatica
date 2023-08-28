package com.example.marcacaohorarios.app.dto.request;

import com.example.marcacaohorarios.domain.annotation.Hour;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClockingRequest {
    @Hour
    String entry;
    @Hour
    String departure;
}
