package ru.liga.currencybase.executor;

import lombok.extern.slf4j.Slf4j;
import ru.liga.currencybase.entity.Currency;
import ru.liga.currencybase.entity.Output;
import ru.liga.currencybase.output.GraphOutput;
import ru.liga.currencybase.output.ListOutput;
import ru.liga.currencybase.output.Outputs;

import java.util.List;

/**
 * Класс для запуска 'вывода'
 */
@Slf4j
public class OutputExecutor {
    /**
     * Выполняем вывод
     *
     * @param output     'вывод'
     * @param currencies список валюты
     * @return String строка/список/картинка
     * @throws IllegalArgumentException "Некорректный 'вывод'"
     */
    public String executeOutput(Output output, List<Currency> currencies) {
        Outputs outputImpl;
        switch (output) {
            case LIST, DEFAULT -> outputImpl = new ListOutput();
            case GRAPH -> outputImpl = new GraphOutput();
            default -> {
                log.error("Ой, забыли написать вывод, для " + output);
                throw new IllegalArgumentException("Ой, забыли написать вывод, для " + output);
            }
        }
        return outputImpl.execute(currencies);
    }
}
