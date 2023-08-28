package com.example.marcacaohorarios.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Clocking {
    String entry;
    String departure;
    List<String> delay;
    List<String> overtime;
}
