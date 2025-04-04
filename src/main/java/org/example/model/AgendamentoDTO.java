package org.example.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoDTO {

    private Rota rota;

    @NotBlank(message = "Origem é obrigatória")
    private String origem;
    @NotBlank(message = "Destino é obrigatório")
    private String destino;


    private String mapaUrl;
    private String tempoEstimado;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dataHora;
    private BigDecimal valor;

    public void gerarMapaUrl(String apiKey) {
        try {
            String encodedOrigem = java.net.URLEncoder.encode(this.origem, java.nio.charset.StandardCharsets.UTF_8);
            String encodedDestino = java.net.URLEncoder.encode(this.destino, java.nio.charset.StandardCharsets.UTF_8);

            this.mapaUrl = String.format(
                    "https://www.google.com/maps/embed/v1/directions?" +
                            "origin=%s&destination=%s&key=%s",
                    encodedOrigem, encodedDestino, apiKey
            );
        } catch (Exception e) {
            this.mapaUrl = "";
        }
    }

    public int getTempoEmMinutos() {
        if (this.tempoEstimado == null) {
            return 0;
        }
        try {
            return Integer.parseInt(this.tempoEstimado.split(" ")[0]);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String toTelegramMessage() {
        return String.format(
                "🚗 *Agendamento de Carona* 🚗\n" +
                        "📍 *Origem:* %s\n" +
                        "🏁 *Destino:* %s\n" +
                        "📅 *Data/Hora:* %s\n" +
                        "📏 *Distância:* %.2f km\n" +
                        "⏱ *Tempo estimado:* %.0f minutos\n" +
                        "💵 *Valor:* R$ %.2f",
                this.origem,
                this.destino,
                this.dataHora != null ?
                        this.dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")) :
                        "Não informado",
                this.rota != null ? this.rota.getDistancia() : 0,
                this.rota != null ? this.rota.getDuracao() : 0,
                this.valor != null ? this.valor.doubleValue() : 0.0
        );
    }
}