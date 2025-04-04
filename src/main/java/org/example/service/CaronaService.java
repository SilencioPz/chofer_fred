package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.AgendamentoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
public class CaronaService {
    @Value("${google.maps.api.key}")
    private String mapsApiKey;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AgendamentoDTO agendarCarona(AgendamentoDTO agendamentoDTO) {
        String origem = agendamentoDTO.getOrigem();
        String destino = agendamentoDTO.getDestino();

        try {
            JsonNode routeResponse = chamarApiRotas(origem, destino);

            if (routeResponse == null) {
                throw new RuntimeException("API de rotas não retornou resposta");
            }

            if (!routeResponse.has("routes") && !routeResponse.has("route")) {
                String errorMsg = routeResponse.has("error_message")
                        ? routeResponse.get("error_message").asText()
                        : "Resposta da API não contém campo de rota";
                throw new RuntimeException("Erro na API de rotas: " + errorMsg);
            }

            JsonNode route = routeResponse.has("routes")
                    ? routeResponse.path("routes").get(0)
                    : routeResponse.path("route");

        } catch (Exception e) {
            log.error("Falha ao processar rota entre {} e {}", origem, destino, e);
            throw new RuntimeException("Falha ao processar rota: " + e.getMessage(), e);
        }
        return agendamentoDTO;
    }

    private JsonNode chamarApiRotas(String origem, String destino) {
        try {
            log.debug("Chamando API de rotas para origem: {} e destino: {}", origem, destino);

            String encodedOrigem = URLEncoder.encode(origem, StandardCharsets.UTF_8);
            String encodedDestino = URLEncoder.encode(destino, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://maps.googleapis.com/maps/api/directions/json?" +
                            "origin=" + encodedOrigem +
                            "&destination=" + encodedDestino +
                            "&key=" + mapsApiKey))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            log.debug("Resposta da API: {}", response.body());

            return objectMapper.readTree(response.body());

        } catch (Exception e) {
            log.error("Erro ao chamar API de rotas", e);
            return null;
        }
    }
}