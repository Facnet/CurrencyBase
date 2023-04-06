package ru.liga.currencybase.command;

import lombok.extern.slf4j.Slf4j;
import ru.liga.currencybase.entity.*;
import ru.liga.currencybase.entity.Operation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

/**
 * Класс для проверки аргументов команды RATE
 */
@Slf4j
public class CheckerRateCommand {
    /**
     * Проверяем код валюты
     *
     * @param value строка, содержащую код валюты
     * @return CurrencyCode код валюты
     * @throws IllegalArgumentException "Неправильный код валюты"
     */
    public CurrencyCode checkCurrencyCode(String value) {
        try {
            return CurrencyCode.valueOf(value.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            log.error("Неправильный код валюты " + e);
            throw new IllegalArgumentException("Показываем спрогнозированные курсы только для: " +
                    "Евро(EUR), " +
                    "Доллар США(USD), " +
                    "Турецкая лира(TRY), " +
                    "Болгарский лев(BGN), " +
                    "Армянский драм(AMD)");
        }
    }

    /**
     * Проверяем операцию
     *
     * @param value строка, содержащую операцию
     * @return Period период
     * @throws IllegalArgumentException "Неправильная операция"
     */
    public Period checkOperation(String value) {
        try {
            return Period.valueOf(value.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            log.error("Неправильная операция " + e);
            throw new IllegalArgumentException("Параметр 'period' должен использоваться с: " +
                    "завтра(tomorrow), " +
                    "на 7 дней(week), " +
                    "месяц(month)");
        }
    }

    /**
     * Проверяем дату
     *
     * @param value строка, содержащую дату
     * @return LocalDate дата
     * @throws IllegalArgumentException "Неправильная дата"
     */
    public LocalDate checkDate(String value) {
        try {
            return LocalDate.parse(value.trim(), Constant.FORMATTER_FOR_PARSE_FILE);
        } catch (DateTimeParseException e) {
            log.error("Неправильная дата " + e);
            throw new IllegalArgumentException("Неправильный формат даты, ожидается 'ДД.ММ.ГГГГ'.");
        }
    }

    /**
     * Проверяем алгоритм
     *
     * @param value строка, содержащую алгоритм
     * @return Algorithm алгоритм
     * @throws IllegalArgumentException "Неправильный алгоритм"
     */
    public Algorithm checkAlgorithm(String value) {
        try {
            return Algorithm.valueOf(value.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            log.error("Неправильный алгоритм " + e);
            throw new IllegalArgumentException("Показываем спрогнозированные курсы только по следующим алгоритмам: " +
                    "Алгоритм 'Старый'(old), " +
                    "Алгоритм 'Прошлогодний'(past_year), " +
                    "Алгоритм 'Мистический'(mist), " +
                    "Алгоритм 'Из интернета'(moon)");
        }
    }

    /**
     * Проверяем 'вывод'
     *
     * @param value строка, содержащую 'вывод'
     * @return Output 'вывод'
     * @throws IllegalArgumentException "Неправильный 'вывод'"
     */
    public Output checkOutput(String value) {
        try {
            return Output.valueOf(value.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            log.error("Неправильный 'вывод' " + e);
            throw new IllegalArgumentException("Вывод спрогнозированных курсов может быть следующим: " +
                    "список(list), " +
                    "картинка-график(graph)");
        }
    }

    /**
     * Проверяем входную строку
     *
     * @param split массив, содержащую параметры и аргументы
     * @throws IllegalArgumentException "Некорректная команда"
     */
    public void checkCommandLength(String[] split) {
        if (split.length < 3 || split.length > 4) {
            log.error("Некорректная команда " + Arrays.toString(split));
            throw new IllegalArgumentException("Некорректная команда. Должно быть минимум 2 параметра, " +
                    "например: TRY -period tomorrow -alg old");
        }
    }

    /**
     * Проверяем период и 'вывод'
     * @throws IllegalArgumentException "Некорректная комбинация параметров и аргументов"
     */
    public void checkOperationAndOutput(Operation operation, Output output) {
        if (operation.isNotNullPeriod()) {
            if (operation.getPeriod().equals(Period.TOMORROW) && !output.equals(Output.DEFAULT)) {
                log.error("Некорректная комбинация параметров и аргументов" + operation + " " + output);
                throw new IllegalArgumentException("Нельзя вместе использовать '-period tomorrow' и '-output list/graph'");
            }
            if (operation.getPeriod().equals(Period.WEEK) && output.equals(Output.DEFAULT)) {
                log.error("Некорректная комбинация параметров и аргументов" + operation + " " + output);
                throw new IllegalArgumentException("Нельзя использовать '-period week' без '-output list/graph'");
            }
            if (operation.getPeriod().equals(Period.MONTH) && output.equals(Output.DEFAULT)) {
                log.error("Некорректная комбинация параметров и аргументов" + operation + " " + output);
                throw new IllegalArgumentException("Нельзя использовать '-period month' без '-output list/graph'");
            }
        }
        if (operation.isNotNullDate() && !output.equals(Output.DEFAULT)) {
            log.error("Некорректная комбинация параметров и аргументов" + operation + " " + output);
            throw new IllegalArgumentException("Нельзя вместе использовать '-date ДД.ММ.ГГГГ' и '-output list/graph'");
        }
    }
}
