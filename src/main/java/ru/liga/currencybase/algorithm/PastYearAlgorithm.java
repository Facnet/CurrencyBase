package ru.liga.currencybase.algorithm;

import ru.liga.currencybase.entity.Constant;
import ru.liga.currencybase.entity.CurrencyCode;
import ru.liga.currencybase.entity.Operation;
import ru.liga.currencybase.entity.Currency;
import ru.liga.currencybase.exception.InsufficientDataException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Алгоритм “Прошлогодний”
 */
public class PastYearAlgorithm implements Algorithms{
    /**
     * Прогнозируем валюту
     *
     * @param operation  операция
     * @param currencies список валюты
     * @return список валюты
     * @throws InsufficientDataException "Не нашли дату"
     * @throws IllegalArgumentException "Неправильная операция"
     */
    @Override
    public List<Currency> execute(CurrencyCode currencyCode, Operation operation, List<Currency> currencies) {
        if (operation.isNotNullDate()) {
            LocalDate pastDate = operation.getDate();
            checkOperationDate(pastDate);
            while (true) {
                for (Currency currency : currencies) {
                    if (currency.getDate().isEqual(pastDate)) {
                        return Collections.singletonList(currency);
                    }
                }
                pastDate = pastDate.minusDays(1);
                if (pastDate.isEqual(Constant.MIN_DATE)) {
                    throw new InsufficientDataException("В файле не была найдена запрашиваемая дата");
                }
            }
        } else {
            throw new IllegalArgumentException("Нельзя вместе использовать '-period tomorrow/week/month' и '-alg past_year'");
        }
    }

    /**
     * Проверяем дату
     *
     * @param operationDate дата, за которую надо показать курс
     * @throws IllegalArgumentException "Недостаточная дата"
     */
    @Override
    public void checkOperationDate(LocalDate operationDate) {
        if (operationDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Дата не должна быть в будущем, мы же смотрим в прошлое)");
        }
        if (operationDate.isBefore(Constant.MIN_DATE)) {
            throw new IllegalArgumentException("Данные в файлах хранятся с 1 января 2005");
        }
    }
}
