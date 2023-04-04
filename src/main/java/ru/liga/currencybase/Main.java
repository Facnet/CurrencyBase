package ru.liga.currencybase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.liga.currencybase.updatehandler.CommandsHandler;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    /**
     * Основная программа
     *
     * @param args не ожидаем параметров
     */
    public static void main(String[] args) {
        logger.info("Стартуем приложение...");
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new CommandsHandler());
        } catch (TelegramApiException e) {
            logger.error(String.valueOf(e));
            throw new RuntimeException(e);
        }
    }
}
