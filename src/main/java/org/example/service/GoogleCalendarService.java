package org.example.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.example.model.AgendamentoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GoogleCalendarService {

    private Calendar calendar;

    public GoogleCalendarService(@Value("${google.calendar.credentials.file}") String credentialsPath)
            throws GeneralSecurityException, IOException {

        if (credentialsPath == null || credentialsPath.isBlank()) {
            throw new IllegalStateException("Caminho das credenciais não configurado");
        }

        InputStream in = getClass().getClassLoader().getResourceAsStream(credentialsPath);
        if (in == null) {
            throw new FileNotFoundException("Arquivo de credenciais não encontrado: " + credentialsPath);
        }

        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                .createScoped(Collections.singleton(CalendarScopes.CALENDAR));

        this.calendar = new Calendar.Builder(
                httpTransport,
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName("Chofer Fred")
                .build();

        log.info("Google Calendar Service inicializado com sucesso");
    }


    public void criarEvento(AgendamentoDTO agendamentoDTO) throws Exception {
        try {
            String rfc3339DateTime = agendamentoDTO.getDataHora()
                    .atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"));

            DateTime googleDateTime = new DateTime(rfc3339DateTime);

            Event evento = new Event()
                    .setSummary("Carona: " + agendamentoDTO.getOrigem() + " → " + agendamentoDTO.getDestino())
                    .setDescription("Valor: R$ " + agendamentoDTO.getValor())
                    .setStart(new EventDateTime().setDateTime(googleDateTime))
                    .setEnd(new EventDateTime().setDateTime(googleDateTime));

            calendar.events().insert("primary", evento).execute();
            log.info("Evento criado no Google Calendar para: {}", agendamentoDTO.getDataHora());

        } catch (Exception e) {
            log.error("Erro ao criar evento no Google Calendar", e);
            throw e;
        }
    }
}