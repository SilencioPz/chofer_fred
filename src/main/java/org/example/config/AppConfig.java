package org.example.config;

import com.google.api.client.util.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${telegram.bot.token}")
    public String telegramToken;

    @Value("${google.maps.api.key}")
    public String mapsApiKey;
}