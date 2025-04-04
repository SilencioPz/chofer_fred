package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RotaResponse {
    private int tempoMinutos;
    private String mapaUrl;
}