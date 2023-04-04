package ru.liga.currencybase.executor;

import lombok.extern.slf4j.Slf4j;
import ru.liga.currencybase.command.RateCommand;
import ru.liga.currencybase.entity.Algorithm;
import ru.liga.currencybase.entity.CurrencyCode;
import ru.liga.currencybase.entity.Operation;
import ru.liga.currencybase.entity.Output;
import ru.liga.currencybase.entity.Currency;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для выполнения команды
 */
@Slf4j
public class RateExecutor {
    private final AlgorithmExecutor algorithmExecutor;
    private final OutputExecutor outputExecutor;

    public RateExecutor(AlgorithmExecutor algorithmExecutor, OutputExecutor outputExecutor) {
        this.algorithmExecutor = algorithmExecutor;
        this.outputExecutor = outputExecutor;
    }

    /**
     * Выполняем команду пользователя
     *
     * @param rateCommand команда
     * @return результат выполнения
     */
    public String execute(RateCommand rateCommand) {
        List<CurrencyCode> currencyCodes = rateCommand.getCurrencyCodes();
        Operation operation = rateCommand.getOperation();
        Algorithm algorithm = rateCommand.getAlgorithm();
        Output output = rateCommand.getOutput();

        List<Currency> result = new ArrayList<>();
        for (CurrencyCode currencyCode : currencyCodes) {
            result.addAll(new ArrayList<>(algorithmExecutor.executeAlgorithm(algorithm, currencyCode, operation)));
        }

        return outputExecutor.executeOutput(output, result);
    }
}
