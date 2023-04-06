package ru.liga.currencybase.entity;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Класс для констант
 */
public class Constant {
    public static final DateTimeFormatter FORMATTER_FOR_PRINT_DATE = DateTimeFormatter.ofPattern("EEE dd.MM.yyyy");
    public static final DateTimeFormatter FORMATTER_FOR_PARSE_FILE = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final int NUMBER_OF_PREVIOUS_COURSES_WEEK = 7;
    public static final int NUMBER_OF_PREVIOUS_COURSES_MONTH = 30;
    public static final String DELIMITER_CSV_FILE = ";";
    public static final Duration DEFAULT_CACHE_TIME = Duration.ofHours(6);
    public static final LocalDate MIN_DATE = LocalDate.parse("01.01.2005", Constant.FORMATTER_FOR_PARSE_FILE);
    public static final int MIN_YEAR = 2023;
    public static final int DEFAULT_IMAGE_WIDTH = 1920;
    public static final int DEFAULT_IMAGE_HEIGHT = 1080;
}
