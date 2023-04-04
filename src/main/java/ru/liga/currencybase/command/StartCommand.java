package ru.liga.currencybase.command;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Эта команда запускает диалог с ботом
 */
public class StartCommand extends ServiceCommand {
    public StartCommand() {
        super("start", "С помощью этой команды вы можете запустить бота");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        StringBuilder message = new StringBuilder("Привет " + getUserName(user));
        sendAnswer(absSender, chat, this.getCommandIdentifier(), user, message);
    }
}
