package ru.liga.currencybase.exception;

/**
 * Класс для кастомной ошибки
 * Неправильный формат CSV-файла
 */
public class CsvFormatException extends RuntimeException {
    public CsvFormatException(String message) {
        super(message);
    }
}
