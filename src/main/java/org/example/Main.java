package org.example;

import org.example.service.TelegramBotService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class Main {
    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        TelegramBotService bot = context.getBean(TelegramBotService.class);

        try {
            bot.enviarMensagem("Bot iniciado com sucesso!");
            System.out.println("✅ Mensagem de teste enviada com sucesso!");
        } catch (Exception e) {
            System.err.println("❌ Falha ao enviar mensagem: " + e.getMessage());
        }
    }
}