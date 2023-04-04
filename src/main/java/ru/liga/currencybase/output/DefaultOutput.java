package ru.liga.currencybase.output;

import ru.liga.currencybase.entity.Currency;

import java.util.List;

/**
 * Выводим одной строкой
 */
public class DefaultOutput implements Outputs {
    /**
     * Выводим валюту
     *
     * @param currencies список строк
     * @return string валюта текстом
     */
    @Override
    public String execute(List<Currency> currencies) {
        return currencies.get(currencies.size() - 1).toString();
    }
}
