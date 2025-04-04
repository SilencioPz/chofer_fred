package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.AgendamentoDTO;
import org.example.service.CaronaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/caronas")
@RequiredArgsConstructor
public class CaronaController {
    private final CaronaService caronaService;

    @PostMapping
    public AgendamentoDTO agendar(@RequestBody AgendamentoDTO dto) {
        return caronaService.agendarCarona(dto);
    }
}