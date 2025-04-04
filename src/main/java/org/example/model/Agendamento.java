package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agendamento {
    private Long id;
    private String parente;
    private LocalDateTime dataHora;
    private String destino;
}