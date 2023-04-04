package ru.liga.currencybase.output;

import ru.liga.currencybase.entity.Currency;

import java.util.List;

public interface Outputs {
    String execute(List<Currency> currencies);
}
