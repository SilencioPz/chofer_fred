package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.example.model.Rota;
import org.example.model.RotaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.http.HttpResponse;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleMapsService {
    @Getter
    @Value("${google.maps.api.key}")
    private String mapsApiKey;

    private final HttpClient httpClient;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(GoogleMapsService.class);

    public GoogleMapsService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public Rota calcularRotaCompleta(String origem, String destino) {
        try {

            JsonNode response = chamarApiDirections(origem, destino);

            if (response == null) {
                throw new IllegalArgumentException("API não retornou resposta.");
            }

            if (response.has("error_message")) {
                String errorMsg = response.get("error_message").asText();
                throw new RuntimeException("Erro na API: " + errorMsg);
            }

            if (!response.has("routes") || response.get("routes").isEmpty()) {
                throw new RuntimeException("Nenhuma rota encontrada entre os pontos");
            }

            JsonNode firstRoute = response.get("routes").get(0);
            JsonNode legs = firstRoute.get("legs").get(0);

            Rota rota = new Rota();
            rota.setOrigem(origem);
            rota.setDestino(destino);
            rota.setDistancia(legs.get("distance").get("value").asDouble() / 1000);
            rota.setDuracao(legs.get("duration").get("value").asDouble() / 60);

            List<String> passos = new ArrayList<>();
            JsonNode steps = legs.get("steps");
            for (JsonNode step : steps) {
                passos.add(step.get("html_instructions").asText()
                        .replaceAll("<[^>]*>", ""));
            }
            rota.setPassos(passos);

            return rota;
        } catch (Exception e) {
            logger.error("Erro ao calcular rota entre {} e {}", origem, destino, e);
            throw new RuntimeException("Erro ao calcular rota: " + e.getMessage(), e);
        }
    }

    private Rota processarRespostaGoogleMaps(JsonNode response) {
        Rota rota = new Rota();

        JsonNode route = response.path("routes").get(0);
        JsonNode leg = route.path("legs").get(0);

        rota.setDistancia(leg.path("distance").path("value").asDouble());
        rota.setDuracao(leg.path("duration").path("value").asDouble());

        return rota;
    }

    public RotaResponse calcularRotaResumida(String origem, String destino) {
        Rota rota = calcularRotaCompleta(origem, destino);
        String mapaUrl = gerarMapaUrl(origem, destino);
        return new RotaResponse((int) Math.round(rota.getDuracao()), mapaUrl);
    }

    private JsonNode chamarApiDirections(String origem, String destino) {
        try {
            String encodedOrigem = URLEncoder.encode(origem, StandardCharsets.UTF_8);
            String encodedDestino = URLEncoder.encode(destino, StandardCharsets.UTF_8);

            String url = String.format(
                    "https://maps.googleapis.com/maps/api/directions/json?" +
                            "origin=%s&destination=%s&key=%s&language=pt-BR",
                    encodedOrigem, encodedDestino, mapsApiKey);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString());

            logger.debug("Resposta da API Directions: {}", response.body());

            return objectMapper.readTree(response.body());

        } catch (Exception e) {
            logger.error("Erro ao chamar API Directions", e);
            return null;
        }
    }

    private String gerarMapaUrl(String origem, String destino) {
        try {
            String encodedOrigem = URLEncoder.encode(origem, StandardCharsets.UTF_8);
            String encodedDestino = URLEncoder.encode(destino, StandardCharsets.UTF_8);

            return String.format(
                    "https://www.google.com/maps/embed/v1/directions?" +
                            "origin=%s&destination=%s&key=%s",
                    encodedOrigem, encodedDestino, mapsApiKey);

        } catch (Exception e) {
            logger.error("Erro ao gerar URL do mapa", e);
            return "";
        }
    }
}