package ru.liga.currencybase.factory;

import ru.liga.currencybase.command.CheckerRateCommand;
import ru.liga.currencybase.command.ParserRateCommand;

/**
 * Класс Factory? для команды ParserRateCommand
 */
public class ParserRateCommandFactory {
    public static ParserRateCommand buildParserRateCommand() {
        CheckerRateCommand checkerRateCommand = new CheckerRateCommand();
        return new ParserRateCommand(checkerRateCommand);
    }
}
