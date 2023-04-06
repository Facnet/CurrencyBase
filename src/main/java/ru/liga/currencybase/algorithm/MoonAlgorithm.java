package ru.liga.currencybase.algorithm;

import lombok.extern.slf4j.Slf4j;
import ru.liga.currencybase.entity.*;
import ru.liga.currencybase.exception.InsufficientDataException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Алгоритм “из интернета”
 */
@Slf4j
public class MoonAlgorithm implements Algorithms {

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
        List<Currency> buffer = new ArrayList<>(currencies.subList(0, Constant.NUMBER_OF_PREVIOUS_COURSES_MONTH));

        int resultSize = buffer.size();
        double[] dates = new double[buffer.size()];
        double[] curs = new double[buffer.size()];

        for (int i = 0; i < resultSize; i++) {
            dates[i] = buffer.get(i).getDate().toEpochDay();
            curs[i] = buffer.get(i).getCurs().doubleValue();
        }

        LinearRegression lr = new LinearRegression(dates, curs);

        List<Currency> result = new ArrayList<>();
        LocalDate nextDate = LocalDate.now().plusDays(1);
        double nextDays = nextDate.toEpochDay();
        if (operation.isNotNullPeriod()) {
            result.addAll(operationPeriod(operation, buffer, nextDate, nextDays, currencyCode, resultSize, lr));
        }
        if (operation.isNotNullDate()) {
            result.addAll(operationDate(operation, buffer, nextDate, nextDays, currencyCode, resultSize, lr));
        }
        return result;
    }

    /**
     * Прогнозируем если задан период
     *
     * @param operation    операция
     * @param buffer       список валюты
     * @param nextDate     следующая дата
     * @param nextDays     количество дней с начала эпохи
     * @param currencyCode код валюты
     * @param resultSize   размер буфера
     * @param lr           объект класса LinearRegression
     * @return список валюты
     */
    private List<Currency> operationPeriod(Operation operation, List<Currency> buffer, LocalDate nextDate, double nextDays, CurrencyCode currencyCode, int resultSize, LinearRegression lr) {
        List<Currency> result = new ArrayList<>();
        for (int i = 0; i < operation.getPeriod().getDayInPeriod(); i++) {
            BigDecimal amount = receiveCurs(lr, nextDays);
            buffer.add(0, new Currency(currencyCode, nextDate, amount));
            result.add(0, new Currency(currencyCode, nextDate, amount));
            buffer.remove(buffer.get(resultSize));
            nextDate = nextDate.plusDays(1);
            nextDays = nextDate.toEpochDay();
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
     * @param nextDays     количество дней с начала эпохи
     * @param currencyCode код валюты
     * @param resultSize   размер буфера
     * @param lr           объект класса LinearRegression
     * @return список валюты
     */
    private List<Currency> operationDate(Operation operation, List<Currency> buffer, LocalDate nextDate, double nextDays, CurrencyCode currencyCode, int resultSize, LinearRegression lr) {
        LocalDate operationDate = operation.getDate();
        checkOperationDate(operationDate);
        do {
            BigDecimal amount = receiveCurs(lr, nextDays);
            buffer.add(0, new Currency(currencyCode, nextDate, amount));
            buffer.remove(buffer.get(resultSize));
            nextDate = nextDate.plusDays(1);
            nextDays = nextDate.toEpochDay();
        } while (operationDate.isAfter(nextDate) || operationDate.isEqual(nextDate));
        return Collections.singletonList(buffer.get(0));
    }

    /**
     * Получаем следующий курс
     *
     * @param lr список валюты, содержащий информацию о курсе валют
     * @return BigDecimal курс
     */
    private BigDecimal receiveCurs(LinearRegression lr, double nextDays) {
        double nextDayCurs = lr.predict(nextDays);
        return toBigDecimal(nextDayCurs);
    }

    /**
     * Проверяем достаточность данных
     *
     * @param currencies список курса валюты
     * @throws InsufficientDataException "Недостаточно данных"
     */
    private void checkInsufficientData(List<Currency> currencies) {
        if (currencies.size() < Constant.NUMBER_OF_PREVIOUS_COURSES_MONTH) {
            log.error("Файл содержит недостаточно данных " + currencies.size());
            throw new InsufficientDataException("Файл содержит недостаточно данных");
        }
    }

    /**
     * Получение курса валюты
     *
     * @param value строка, содержащее курс
     * @return курс валюты, округленный до 2
     */
    private BigDecimal toBigDecimal(double value) {
        BigDecimal result = BigDecimal.valueOf(value);
        return result.setScale(2, RoundingMode.HALF_UP);
    }
}
