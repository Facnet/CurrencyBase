package ru.liga.currencybase.updatehandler;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.currencybase.telegram.BotConfig;
import ru.liga.currencybase.command.HelpCommand;
import ru.liga.currencybase.command.RateServiceCommand;
import ru.liga.currencybase.command.StartCommand;

/**
 * Обработчик работает с командами
 */
@Slf4j
public class CommandsHandler extends TelegramLongPollingCommandBot {

    private static String CURRENCY_BASE_TOKEN = "123:123";

    public CommandsHandler() {
        register(new StartCommand());
        register(new RateServiceCommand());
        HelpCommand helpCommand = new HelpCommand(this);
        register(helpCommand);

        registerDefaultAction((absSender, message) -> {
            String echoMessage = "Команда '" + message.getText() + "' не известна этому боту. А вот и помощь.";
            sendAnswer(message, echoMessage);
            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[]{});
        });
    }

    /**
     * Ответ на запрос, не являющийся командой
     */
    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                String echoMessage = "Привет, можешь начать с команды: /help";
                sendAnswer(message, echoMessage);
            }
        }
    }

    @Override
    public String getBotToken() {
        return BotConfig.CURRENCY_BASE_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return BotConfig.CURRENCY_BASE_USER;
    }

    /**
     * Отправка ответа
     *
     * @param message message
     * @param text    текст ответа
     */
    private void sendAnswer(Message message, String text) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        answer.setText(text);
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            log.error(getUserName(message) + " " + e);
        }
    }

    /**
     * Формирование имени пользователя
     *
     * @param message сообщение
     */
    private String getUserName(Message message) {
        User user = message.getFrom();
        String userName = user.getUserName();
        return (userName != null) ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
    }
}
