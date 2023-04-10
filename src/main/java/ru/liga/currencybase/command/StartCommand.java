package ru.liga.currencybase.command;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.liga.currencybase.entity.CommandIdentifier;

/**
 * Эта команда запускает диалог с ботом
 */
public class StartCommand extends ServiceCommand {
    public StartCommand() {
        super(CommandIdentifier.START, "С помощью этой команды вы можете запустить бота");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        StringBuilder message = new StringBuilder("Привет " + getUserName(user));
        sendAnswer(absSender, chat.getId().toString(), this.getCommandIdentifier(), user, message);
    }
}
