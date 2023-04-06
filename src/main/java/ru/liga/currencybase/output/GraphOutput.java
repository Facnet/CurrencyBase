package ru.liga.currencybase.output;

import ru.liga.currencybase.entity.Currency;

import java.util.List;

/**
 * Выводим картинкой
 */
public class GraphOutput implements Outputs {
    private final CurrencyLineChart currencyLineChart = new CurrencyLineChart();

    /**
     * Выводим валюту
     *
     * @param currencies список строк
     * @return кастомный текст
     */
    @Override
    public String execute(List<Currency> currencies) {
        currencyLineChart.saveJpeg(currencies);
        return "Вот диаграмма";
    }
}
