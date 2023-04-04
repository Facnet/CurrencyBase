package ru.liga.currencybase.algorithm;

import ru.liga.currencybase.entity.CurrencyCode;
import ru.liga.currencybase.entity.Operation;
import ru.liga.currencybase.entity.Currency;

import java.time.LocalDate;
import java.util.List;

public interface Algorithms {

    List<Currency> execute(CurrencyCode currencyCode, Operation operation, List<Currency> currencies);

    default void checkOperationDate(LocalDate operationDate) {
        if (operationDate.isBefore(LocalDate.now().plusDays(1))) {
            throw new IllegalArgumentException("Дата должна быть в будущем, мы же прогнозируем)");
        }
    }

}
