package ru.liga.currencybase.executor;

import ru.liga.currencybase.entity.Output;
import ru.liga.currencybase.entity.Currency;
import ru.liga.currencybase.output.DefaultOutput;
import ru.liga.currencybase.output.GraphOutput;
import ru.liga.currencybase.output.ListOutput;
import ru.liga.currencybase.output.Outputs;

import java.util.List;

/**
 * Класс для запуска 'вывода'
 */
public class OutputExecutor {
    /**
     * Выполняем вывод
     * @param output 'вывод'
     * @param currencies список валюты
     * @return String строка/список/картинка
     * @throws IllegalArgumentException "Некорректный 'вывод'"
     */
    public String executeOutput(Output output, List<Currency> currencies){
        Outputs outputImpl;
        switch (output){
            case LIST -> outputImpl = new ListOutput();
            case GRAPH -> outputImpl = new GraphOutput();
            case DEFAULT -> outputImpl = new DefaultOutput();
            default -> throw new IllegalArgumentException("Ой, забыли написать вывод, для " + output);
        }
        return outputImpl.execute(currencies);
    }
}
