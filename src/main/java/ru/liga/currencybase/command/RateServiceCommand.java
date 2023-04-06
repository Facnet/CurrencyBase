package ru.liga.currencybase.command;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.liga.currencybase.entity.CommandIdentifier;
import ru.liga.currencybase.entity.Output;
import ru.liga.currencybase.executor.RateExecutor;
import ru.liga.currencybase.factory.RateFactory;

import java.util.Arrays;

/**
 * Эта команда запускает прогнозирование курса валют
 */
@Slf4j
public class RateServiceCommand extends ServiceCommand {
    private final RateExecutor rateExecutor;
    private final ParserRateCommand parserRateCommand;

    public RateServiceCommand() {
        super(CommandIdentifier.RATE.name().toLowerCase(), """
                Алгоритм прогнозирования курса валют.
                rate 'код_валюты' -period tomorrow/week/month -alg old/past_year/mist -output list/graph
                rate 'код_валюты' -date 'День.Месяц.Год' -alg old/past_year/mist -output list/graph""");
        this.rateExecutor = RateFactory.buildRateExecutor();
        this.parserRateCommand = new ParserRateCommand(new CheckerRateCommand());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        log.info("выполняем команду:" + Arrays.toString(strings));
        StringBuilder messageBuilder = new StringBuilder();
        try {
            String parameters = String.join(" ", strings);

            messageBuilder.append(getUserName(user)).append(", вот результат команды:\n");
            RateCommand rateCommand = parserRateCommand.parseRateCommand(parameters);
            String result = rateExecutor.execute(rateCommand);

            messageBuilder.append(result);

            if (rateCommand.getOutput().equals(Output.GRAPH)) {
                sendImage(absSender, this.getCommandIdentifier(), user, chat);
            }
        } catch (IllegalArgumentException e) {
            messageBuilder.append(" ОШИБКА: ").append(e.getMessage());
            log.error(messageBuilder.toString());
        } catch (RuntimeException e) {
            messageBuilder.append(" КРИТИЧЕСКАЯ ОШИБКА: ").append(e.getMessage());
            log.error(messageBuilder.toString());
        }
        sendAnswer(absSender, chat.getId().toString(), this.getCommandIdentifier(), user, messageBuilder);
        log.info("выполнили команду:" + Arrays.toString(strings));
    }
}
