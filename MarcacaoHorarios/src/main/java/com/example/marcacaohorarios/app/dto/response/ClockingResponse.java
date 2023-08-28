package com.example.marcacaohorarios.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClockingResponse {
    String entry;
    String departure;
    List<String> delay;
    List<String> overtime;
}
