package ru.liga.currencybase.exception;

/**
 * Класс для кастомной ошибки
 * Файл содержит недостаточно данных
 */
public class InsufficientDataException extends RuntimeException {
    public InsufficientDataException(String message) {
        super(message);
    }
}
