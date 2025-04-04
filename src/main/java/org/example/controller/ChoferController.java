package org.example.controller;

import org.example.model.AgendamentoDTO;
import org.example.model.Rota;
import org.example.service.CaronaService;
import org.example.service.GoogleCalendarService;
import org.example.service.GoogleMapsService;
import org.example.service.TelegramBotService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Controller
@Slf4j
public class ChoferController {

    private final CaronaService caronaService;
    private final GoogleMapsService googleMapsService;
    private final GoogleCalendarService googleCalendarService;
    private final TelegramBotService telegramBotService;

    @Value("${google.maps.api.key}")
    private String mapsApiKey;

    public ChoferController(CaronaService caronaService,
                            GoogleMapsService googleMapsService,
                            TelegramBotService telegramBotService) {
        this.caronaService = caronaService;
        this.googleMapsService = googleMapsService;
        this.googleCalendarService = null;
        this.telegramBotService = telegramBotService;
    }

    @GetMapping("/chofer")
    public String mostrarPaginaChofer(Model model) {
        model.addAttribute("agendamentoDTO", new AgendamentoDTO());
        return "chofer";
    }

    @PostMapping(value = "/agendar", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String agendarCarona(@ModelAttribute AgendamentoDTO agendamentoDTO,
                                @RequestParam("dataHora") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
                                LocalDateTime dataHora, Model model) {
        try {
            if (agendamentoDTO.getOrigem() == null || agendamentoDTO.getOrigem().trim().isEmpty() ||
                    agendamentoDTO.getDestino() == null || agendamentoDTO.getDestino().trim().isEmpty()) {

                model.addAttribute("error", "Origem e destino são obrigatórios");
                return "chofer";
            }

            Rota rota = googleMapsService.calcularRotaCompleta(
                    agendamentoDTO.getOrigem(),
                    agendamentoDTO.getDestino());
                    agendamentoDTO.setDataHora(dataHora);

            agendamentoDTO.setRota(rota);
            agendamentoDTO.setTempoEstimado(rota.getDuracao() + " minutos");
            agendamentoDTO.gerarMapaUrl(mapsApiKey);

            AgendamentoDTO resultado = caronaService.agendarCarona(agendamentoDTO);

            model.addAttribute("agendamentoDTO", resultado);
            model.addAttribute("success", "Agendamento realizado com sucesso!");

            telegramBotService.enviarMensagem(resultado.toTelegramMessage());

        } catch (Exception e) {
            log.error("Erro no agendamento", e);
            model.addAttribute("error", "Erro ao calcular rota: " + e.getMessage());
            model.addAttribute("agendamentoDTO", agendamentoDTO);
        }

        return "chofer";
    }
}