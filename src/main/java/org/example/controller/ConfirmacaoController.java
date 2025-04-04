package org.example.controller;

import com.google.common.base.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.model.AgendamentoDTO;
import org.example.service.GoogleCalendarService;
import org.example.service.TelegramBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/confirmacoes")
@Slf4j
public class ConfirmacaoController {

    private final GoogleCalendarService googleCalendarService;
    private final TelegramBotService telegramBotService;

    public ConfirmacaoController(
            @Autowired(required = false) GoogleCalendarService googleCalendarService,
            TelegramBotService telegramBotService
    ) {
        this.googleCalendarService = googleCalendarService;
        this.telegramBotService = telegramBotService;
    }

    @PostMapping("/agendamento")
    public String confirmar(@ModelAttribute AgendamentoDTO agendamentoDTO, Model model,
                            RedirectAttributes redirectAttributes) {
        try {
            if (agendamentoDTO.getOrigem() == null || agendamentoDTO.getDestino() == null) {
                throw new IllegalArgumentException("Dados incompletos para confirmação");
            }

            if (googleCalendarService != null) {
                try {
                    googleCalendarService.criarEvento(agendamentoDTO);
                    model.addAttribute("success", "Evento criado no Google Calendar!");
                } catch (Exception e) {
                    log.error("Erro ao criar evento no Google Calendar", e);
                }
            } else {
                log.warn("Google Calendar Service não disponível - pulando criação de evento");
            }

            String mensagem = "✅ Agendamento CONFIRMADO\n" +
                    "Origem: " + agendamentoDTO.getOrigem() + "\n" +
                    "Destino: " + agendamentoDTO.getDestino() + "\n" +
                    "Data/Hora: " + agendamentoDTO.getDataHora() + "\n" +
                    "Valor: R$ " + agendamentoDTO.getValor();

            telegramBotService.enviarMensagem(mensagem);
            redirectAttributes.addFlashAttribute("success", "Agendamento confirmado com sucesso!");
        } catch (Exception e) {
            log.error("Erro ao confirmar agendamento", e);
            redirectAttributes.addFlashAttribute("error",
                    "Erro durante a confirmação: " + e.getMessage());
        }

        return "redirect:/chofer";
    }
}