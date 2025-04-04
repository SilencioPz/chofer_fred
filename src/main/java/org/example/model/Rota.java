package org.example.model;

import lombok.Data;

import java.util.List;

@Data
public class Rota {
    private String origem;
    private String destino;
    private double distancia;
    private double duracao;
    private List<String> passos;
}