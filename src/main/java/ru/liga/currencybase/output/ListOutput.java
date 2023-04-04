package ru.liga.currencybase.output;

import ru.liga.currencybase.entity.Currency;

import java.util.Collections;
import java.util.List;

/**
 * Выводим списком
 */
public class ListOutput implements Outputs {
    /**
     * Выводим валюту
     *
     * @param currencies список строк
     * @return string список валюты текстом
     */
    @Override
    public String execute(List<Currency> currencies) {
        Collections.reverse(currencies);
        StringBuilder buffer = new StringBuilder();
        for (Currency currency : currencies) {
            buffer.append(currency).append("\n");
        }
        return buffer.toString();
    }
}
