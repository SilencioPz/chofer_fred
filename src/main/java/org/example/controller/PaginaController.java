package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.AgendamentoDTO;
import org.example.service.CaronaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PaginaController {
    private final CaronaService caronaService;

    @GetMapping("/teste")
    public String mostrarPaginaTeste(Model model) {
        AgendamentoDTO agendamento = caronaService.agendarCarona(
                AgendamentoDTO.builder()
                        .origem("Av. Presidente Dutra, Rondonópolis")
                        .destino("Av. Rio Branco, Rondonópolis")
                        .build()
        );
        model.addAttribute("agendamento", agendamento);
        return "teste";
    }
}