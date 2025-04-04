package org.example.controller;

import org.example.model.Rota;
import org.example.service.GoogleMapsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TesteController {

    @Autowired
    private GoogleMapsService googleMapsService;

    @GetMapping("/teste-rota")
    public String paginaTeste(Model model) {
        model.addAttribute("googleMapsKey", googleMapsService.getMapsApiKey());
        return "teste-rota";
    }

    @GetMapping("/api/calcular-rota")
    @ResponseBody
    public Rota calcularRota(
            @RequestParam String origem,
            @RequestParam String destino) {
        return googleMapsService.calcularRotaCompleta(origem, destino);
    }
}