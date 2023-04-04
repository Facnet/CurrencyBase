package ru.liga.currencybase.command;

import lombok.extern.slf4j.Slf4j;
import ru.liga.currencybase.entity.Algorithm;
import ru.liga.currencybase.entity.CurrencyCode;
import ru.liga.currencybase.entity.Operation;
import ru.liga.currencybase.entity.Output;

import java.util.List;

/**
 * Класс команды
 */
@Slf4j
public class RateCommand {
    private final List<CurrencyCode> currencyCodes;
    private final Operation operation;
    private final Algorithm algorithm;
    private final Output output;

    public RateCommand(List<CurrencyCode> currencyCodes, Operation operation, Algorithm algorithm, Output output) {
        this.currencyCodes = currencyCodes;
        this.operation = operation;
        this.algorithm = algorithm;
        this.output = output;
    }

    public List<CurrencyCode> getCurrencyCodes() {
        return currencyCodes;
    }

    public Operation getOperation() {
        return operation;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public Output getOutput() {
        return output;
    }

    @Override
    public String toString() {
        return "RateCommand{" + "currencyCodes=" + currencyCodes + ", operation=" + operation + ", algorithm=" + algorithm + ", output=" + output + '}';
    }
}
