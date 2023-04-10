package ru.liga.currencybase.command;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.liga.currencybase.entity.CommandIdentifier;


/**
 * Эта команда помогает пользователю найти нужную ему команду
 */
public class HelpCommand extends ServiceCommand {
    private final ICommandRegistry commandRegistry;

    public HelpCommand(ICommandRegistry commandRegistry) {
        super(CommandIdentifier.HELP.name(), "Получите все команды, которые предоставляет этот бот");
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        StringBuilder helpMessageBuilder = new StringBuilder("<b>Help</b>\n");
        helpMessageBuilder.append("Это зарегистрированные команды для этого бота:\n\n");

        for (IBotCommand botCommand : commandRegistry.getRegisteredCommands()) {
            helpMessageBuilder.append(botCommand.toString()).append("\n\n");
        }

        sendAnswer(absSender, chat.getId().toString(), this.getCommandIdentifier(), user, helpMessageBuilder);
    }
}
