package org.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TelegramBotService {

    private final TelegramBot telegramBot;
    private final String chatId;

    public TelegramBotService(
            TelegramBot telegramBot,
            @Value("${telegram.chat.id}") String chatId) {
        this.telegramBot = telegramBot;
        this.chatId = chatId;
        log.info("Telegram Bot Service inicializado. Chat ID: {}", chatId);
    }

    public void enviarMensagem(String mensagem) {
        try {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(mensagem);
            message.enableMarkdown(true);

            // Cast seguro para TelegramLongPollingBot
            if (telegramBot instanceof TelegramLongPollingBot) {
                ((TelegramLongPollingBot) telegramBot).execute(message);
                log.info("Mensagem enviada para o Telegram com sucesso");
            } else {
                throw new UnsupportedOperationException("Tipo de TelegramBot não suportado");
            }
        } catch (TelegramApiException e) {
            log.error("Erro ao enviar mensagem para o Telegram", e);
            throw new RuntimeException("Falha ao enviar mensagem para o Telegram", e);
        }
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getUserName();
            log.info("Mensagem recebida de: {} (Chat ID: {})", userName, chatId);
        }
    }
}