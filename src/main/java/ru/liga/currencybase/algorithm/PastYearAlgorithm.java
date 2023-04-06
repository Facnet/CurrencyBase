package ru.liga.currencybase.algorithm;

import lombok.extern.slf4j.Slf4j;
import ru.liga.currencybase.entity.*;
import ru.liga.currencybase.exception.InsufficientDataException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Алгоритм “Прошлогодний”
 */
@Slf4j
public class PastYearAlgorithm implements Algorithms {

    /**
     * Прогнозируем валюту
     *
     * @param operation  операция
     * @param currencies список валюты
     * @return список валюты
     */
    @Override
    public List<Currency> execute(CurrencyCode currencyCode, Operation operation, List<Currency> currencies) {
        List<Currency> result = new ArrayList<>();
        if (operation.isNotNullDate()) {
            result.addAll(operationDate(operation, currencies));
        }
        if (operation.isNotNullPeriod()) {
            result.addAll(operationPeriod(operation, currencies));
        }
        return result;
    }

    /**
     * Прогнозируем если задан период
     *
     * @param operation операция
     * @param buffer    список валют
     * @return список валют
     */
    private List<Currency> operationPeriod(Operation operation, List<Currency> buffer) {
        List<Currency> result = new ArrayList<>();
        LocalDate nextDate = LocalDate.now().minusYears(1).plusDays(1);
        if (nextDate.getYear() > Constant.MIN_YEAR) {
            nextDate = LocalDate.of(Constant.MIN_YEAR, nextDate.getMonth(), nextDate.getDayOfMonth());
        }
        for (int i = 0; i < operation.getPeriod().getDayInPeriod(); i++) {
            Currency currency = receiveCurrency(buffer, nextDate);
            result.add(0, currency);
            nextDate = nextDate.plusDays(1);
        }
        if (operation.getPeriod().equals(Period.TOMORROW)) {
            return Collections.singletonList(result.get(0));
        }
        return result;
    }

    /**
     * Прогнозируем если задана дата
     *
     * @param operation  операция
     * @param currencies список валюты
     * @return список валют
     */
    private List<Currency> operationDate(Operation operation, List<Currency> currencies) {
        LocalDate pastDate = operation.getDate().minusYears(1);
        checkOperationDate(pastDate);
        if (pastDate.getYear() > Constant.MIN_YEAR) {
            pastDate = LocalDate.of(Constant.MIN_YEAR, pastDate.getMonth(), pastDate.getDayOfMonth());
        }
        return Collections.singletonList(receiveCurrency(currencies, pastDate));
    }

    /**
     * Получаем валюту за прошлый год
     *
     * @param currencies список валюты
     * @param date       дата
     * @return валюта
     * @throws InsufficientDataException "Не нашли дату"
     */
    private Currency receiveCurrency(List<Currency> currencies, LocalDate date) {
        while (true) {
            for (Currency currency : currencies) {
                if (currency.getDate().isEqual(date)) {
                    return currency;
                }
            }
            date = date.minusDays(1);
            if (date.isEqual(Constant.MIN_DATE)) {
                log.error("В файле не была найдена запрашиваемая дата");
                throw new InsufficientDataException("В файле не была найдена запрашиваемая дата");
            }
        }
    }

    /**
     * Проверяем дату
     *
     * @param localDate дату
     * @throws IllegalArgumentException "Некорректная дата"
     */
    @Override
    public void checkOperationDate(LocalDate localDate) {
        if (localDate.isBefore(Constant.MIN_DATE.minusDays(1))) {
            log.error("Некорректная дата " + localDate);
            throw new IllegalArgumentException("Дата должна быть после 31 декабря 2005");
        }
    }
}
