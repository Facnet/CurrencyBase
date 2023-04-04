package ru.liga.currencybase.command;

import ru.liga.currencybase.entity.Algorithm;
import ru.liga.currencybase.entity.CurrencyCode;
import ru.liga.currencybase.entity.Operation;
import ru.liga.currencybase.entity.Output;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для парсинга аргументов команды RATE
 */
public class ParserRateCommand {
    private final CheckerRateCommand checkerRateCommand;

    public ParserRateCommand(CheckerRateCommand checkerRateCommand) {
        this.checkerRateCommand = checkerRateCommand;
    }

    /**
     * Получаем значения
     *
     * @param input массив, содержащую параметры и аргументы
     */
    public RateCommand parseRateCommand(String input) {
        String[] split = input.split("-");
        checkerRateCommand.checkCommandLength(split);

        List<CurrencyCode> currencyCodes = receiveCurrencyCode(split);
        Operation operation = receiveOperation(split);
        Algorithm algorithm = receiveAlgorithm(split);
        Output output = receiveOutput(split);
        checkerRateCommand.checkOperationAndOutput(operation, output);

        return new RateCommand(currencyCodes, operation, algorithm, output);
    }

    /**
     * Получаем код валюты
     *
     * @param input строки, с аргументами
     * @return List CurrencyCode список код валюты
     */
    private List<CurrencyCode> receiveCurrencyCode(String[] input) {
        String[] buffer = input[0].split(",");
        List<CurrencyCode> result = new ArrayList<>();
        for (String value : buffer) {
            result.add(checkerRateCommand.checkCurrencyCode(value));
        }
        return result;
    }

    /**
     * Получаем период, за который надо считать курс валют
     *
     * @param input строки, с аргументами
     * @return Operation, которую вызвал пользователь
     * @throws IllegalArgumentException "Некорректная команда"
     */
    private Operation receiveOperation(String[] input) {
        String[] buffer = input[1].split(" ");
        if (buffer[0].equals("period")) {
            return new Operation(checkerRateCommand.checkOperation(buffer[1]));
        }
        if (buffer[0].equals("date")) {
            return new Operation(checkerRateCommand.checkDate(buffer[1]));
        }
        throw new IllegalArgumentException("Некорректная команда. Первый параметр должен быть 'date' или 'period'");
    }

    /**
     * Получаем алгоритм, по которому надо считать курс валют
     *
     * @param input строки, с аргументами
     * @return Algorithm, которую вызвал пользователь
     * @throws IllegalArgumentException "Некорректная команда"
     */
    private Algorithm receiveAlgorithm(String[] input) {
        String[] buffer = input[2].split(" ");
        if (buffer[0].equals("alg")) {
            return checkerRateCommand.checkAlgorithm(buffer[1]);
        }
        throw new IllegalArgumentException("Некорректная команда. Второй параметр должен быть 'alg'");
    }

    /**
     * Получаем 'вывод', по которому надо выводить курс
     *
     * @param input строки, с аргументами
     * @return Output, которую вызвал пользователь
     * @throws IllegalArgumentException "Некорректная команда"
     */
    private Output receiveOutput(String[] input) {
        if (input.length == 4) {
            String[] buffer = input[3].split(" ");
            if (buffer[0].equals("output")) {
                return checkerRateCommand.checkOutput(buffer[1]);
            } else {
                throw new IllegalArgumentException("Некорректная команда. Третий параметр должен быть 'output'");
            }
        }
        return Output.DEFAULT;
    }
}
