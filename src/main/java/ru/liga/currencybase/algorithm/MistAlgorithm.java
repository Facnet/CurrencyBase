package ru.liga.currencybase.algorithm;

import ru.liga.currencybase.entity.*;
import ru.liga.currencybase.exception.InsufficientDataException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Алгоритм “Мистический”
 */
public class MistAlgorithm implements Algorithms {
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
        List<Currency> result = new ArrayList<>();
        if (operation.isNotNullPeriod()) {
            result.addAll(operationPeriod(operation, currencies, currencyCode));
        }
        if (operation.isNotNullDate()) {
            result.addAll(operationDate(operation, currencies, currencyCode));
        }
        return result;
    }

    /**
     * Прогнозируем если задан период
     *
     * @param operation    операция
     * @param buffer       список валюты
     * @param currencyCode код валюты
     * @return список валюты
     */
    private List<Currency> operationPeriod(Operation operation, List<Currency> buffer, CurrencyCode currencyCode) {
        List<Currency> result = new ArrayList<>();
        LocalDate nextDate = LocalDate.now().plusDays(1);
        for (int i = 0; i < operation.getPeriod().getDayInPeriod(); i++) {
            int currentDay = nextDate.getDayOfMonth();
            List<Currency> currencyOfDay = receiveMatchCurrency(buffer, currentDay);
            BigDecimal amount = receiveRandomCurs(currencyOfDay);
            result.add(0, new Currency(currencyCode, nextDate, amount));
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
     * @param currencies   список валюты
     * @param currencyCode код валюты
     * @return список валюты
     */
    private List<Currency> operationDate(Operation operation, List<Currency> currencies, CurrencyCode currencyCode) {
        LocalDate operationDate = operation.getDate();
        checkOperationDate(operationDate);
        List<Currency> buffer = receiveMatchCurrency(currencies, operationDate.getDayOfMonth());
        BigDecimal amount = receiveRandomCurs(buffer);
        Currency result = new Currency(currencyCode, operationDate, amount);
        return Collections.singletonList(result);
    }

    /**
     * Получаем случайный курс
     *
     * @param currencies список валюты
     * @return валюта
     */
    private BigDecimal receiveRandomCurs(List<Currency> currencies) {
        Random random = new Random();
        int randomIndex = random.nextInt(currencies.size());
        return toBigDecimal(currencies.get(randomIndex).getCurs());
    }

    /**
     * Получаем список валют за этот календарный день?
     *
     * @param currencies список валют
     * @param currentDay день
     * @return список валют
     * @throws InsufficientDataException "Не было найдено значение"
     */
    private List<Currency> receiveMatchCurrency(List<Currency> currencies, int currentDay) {
        List<Currency> buffer = new ArrayList<>();
        for (Currency currency : currencies) {
            if (currency.getDate().getDayOfMonth() == currentDay) {
                buffer.add(currency);
            }
        }
        if (buffer.isEmpty()) {
            throw new InsufficientDataException("Не было найдено значение предыдущих лет за этот календарный день");
        }
        return buffer;
    }

    /**
     * Получение курса валюты
     *
     * @param value BigDecimal, содержащее курс
     * @return курс валюты, округленный до 2
     */
    private BigDecimal toBigDecimal(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
