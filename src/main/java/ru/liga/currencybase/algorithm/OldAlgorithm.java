package ru.liga.currencybase.algorithm;

import lombok.extern.slf4j.Slf4j;
import ru.liga.currencybase.entity.Constant;
import ru.liga.currencybase.entity.CurrencyCode;
import ru.liga.currencybase.entity.Operation;
import ru.liga.currencybase.entity.Period;
import ru.liga.currencybase.entity.Currency;
import ru.liga.currencybase.exception.InsufficientDataException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Алгоритм “Старый”
 */
@Slf4j
public class OldAlgorithm implements Algorithms {
    /**
     * Прогнозируем валюту
     *
     * @param currencyCode код валюты
     * @param operation    операция
     * @param currencies   список валюты
     * @return список валюты
     */
    @Override
    public List<Currency> execute(CurrencyCode currencyCode, Operation operation, List<Currency> currencies) {
        checkInsufficientData(currencies);
        List<Currency> buffer = new ArrayList<>(currencies.subList(0, Constant.NUMBER_OF_PREVIOUS_COURSES_WEEK));

        int resultSize = buffer.size();
        LocalDate nextDate = LocalDate.now().plusDays(1);

        List<Currency> result = new ArrayList<>();
        if (operation.isNotNullPeriod()) {
            result.addAll(operationPeriod(operation, buffer, nextDate, currencyCode, resultSize));
        }
        if (operation.isNotNullDate()) {
            result.addAll(operationDate(operation, buffer, nextDate, currencyCode, resultSize));
        }
        return result;
    }

    /**
     * Прогнозируем если задан период
     *
     * @param operation    операция
     * @param buffer       список валюты
     * @param nextDate     следующая дата
     * @param currencyCode код валюты
     * @param resultSize   размер буфера
     * @return список валюты
     */
    private List<Currency> operationPeriod(Operation operation, List<Currency> buffer, LocalDate nextDate, CurrencyCode currencyCode, int resultSize) {
        List<Currency> result = new ArrayList<>();
        for (int i = 0; i < operation.getPeriod().getDayInPeriod(); i++) {
            BigDecimal amount = receiveCurs(buffer);
            buffer.add(0, new Currency(currencyCode, nextDate, amount));
            result.add(0, new Currency(currencyCode, nextDate, amount));
            buffer.remove(buffer.get(resultSize));
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
     * @param operation    операция
     * @param buffer       список валюты
     * @param nextDate     следующая дата
     * @param currencyCode код валюты
     * @param resultSize   размер буфера
     * @return список валюты
     */
    private List<Currency> operationDate(Operation operation, List<Currency> buffer, LocalDate nextDate, CurrencyCode currencyCode, int resultSize) {
        LocalDate operationDate = operation.getDate();
        checkOperationDate(operationDate);
        do {
            BigDecimal amount = receiveCurs(buffer);
            buffer.add(0, new Currency(currencyCode, nextDate, amount));
            buffer.remove(buffer.get(resultSize));
            nextDate = nextDate.plusDays(1);
        } while (operationDate.isAfter(nextDate) || operationDate.isEqual(nextDate));
        return Collections.singletonList(buffer.get(0));
    }

    /**
     * Проверяем достаточность данных
     *
     * @param currencies список курса валюты
     * @throws InsufficientDataException "Недостаточно данных"
     */
    private void checkInsufficientData(List<Currency> currencies) {
        if (currencies.size() < Constant.NUMBER_OF_PREVIOUS_COURSES_WEEK) {
            throw new InsufficientDataException("Файл содержит недостаточно данных");
        }
    }

    /**
     * Получаем среднее арифметическое значение
     *
     * @param currencies список валюты, содержащий информацию о курсе валют
     * @return число с плавающей точкой, среднее арифметическое значение
     */
    private BigDecimal receiveCurs(List<Currency> currencies) {
        return currencies.stream()
                .map(Currency::getCurs)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(currencies.size()), 2, RoundingMode.HALF_UP);
    }
}
