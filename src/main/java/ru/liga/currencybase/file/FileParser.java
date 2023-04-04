package ru.liga.currencybase.file;

import ru.liga.currencybase.entity.Constant;
import ru.liga.currencybase.entity.CurrencyCode;
import ru.liga.currencybase.entity.Currency;
import ru.liga.currencybase.exception.CsvFormatException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с файлом
 */
public class FileParser {
    private final FileReader fileReader;

    public FileParser(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    /**
     * Получаем список валюты
     *
     * @param currencyCode код валюты
     * @return список валют
     * @throws CsvFormatException "Неправильный формат CSV-файла"
     */
    public List<Currency> parseCurrencyFromFile(String currencyCode) {
        String csvFilePath = "/csv/" + currencyCode + ".csv";
        List<String> stringsFromCsvFile = fileReader.readPreviousCourses(csvFilePath);

        List<Currency> currencies = new ArrayList<>();
        for (String stringFromFile : stringsFromCsvFile) {
            String[] buffer = stringFromFile.split(Constant.DELIMITER_CSV_FILE);
            if (buffer.length != 4) {
                throw new CsvFormatException("Неправильный формат CSV-файла, ожидается 'nominal;data;curs;cdx' " + csvFilePath);
            }
            currencies.add(new Currency(CurrencyCode.valueOf(currencyCode),toLocalDate(buffer[1]), toBigDecimal(buffer[2])));
        }
        return currencies;
    }

    /**
     * Получение курса валюты
     *
     * @param value строка, содержащее курс
     * @return курс валюты
     * @throws RuntimeException "Неправильный формат курса"
     */
    private BigDecimal toBigDecimal(String value) {
        try {
            return new BigDecimal(value.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Неправильный формат курса в файле, ожидается запятая " + value);
        }
    }

    /**
     * Получение даты
     *
     * @param value строка, содержащее дату
     * @return дата
     * @throws DateTimeParseException "Неправильный формат даты"
     */
    private LocalDate toLocalDate(String value) {
        try {
            return LocalDate.parse(value, Constant.FORMATTER_FOR_PARSE_FILE);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("Неправильный формат даты в файле, ожидается 'ДД.ММ.ГГГГ'." + value, value, e.getErrorIndex());
        }
    }
}
