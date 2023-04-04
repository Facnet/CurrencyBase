package ru.liga.currencybase.command;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

/**
 * Суперкласс для сервисных команд
 */
@Slf4j
abstract class ServiceCommand extends BotCommand {
    public ServiceCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    /**
     * Отправка ответа пользователю
     */
    void sendAnswer(AbsSender absSender, Chat chat, String commandName, User user, StringBuilder messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());
        message.enableHtml(true);
        message.setText(messageText.toString());
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            log.error(getUserName(user) + " " + commandName + " " + e);
        }
    }
    /**
     * Отправка фото пользователю
     */
    void sendImage(AbsSender absSender, String commandName, User user, Chat chat){
        try {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chat.getId());
            sendPhoto.setPhoto(new InputFile(new File("line_Chart.jpeg")));
            absSender.execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error(getUserName(user) + " " + commandName + " " + e);
        }
    }

    /**
     * Формирование имени пользователя
     */
    String getUserName(User user) {
        String userName = user.getUserName();
        return (userName != null) ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
    }
}