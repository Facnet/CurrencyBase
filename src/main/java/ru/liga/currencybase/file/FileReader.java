package ru.liga.currencybase.file;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/**
 * Класс для работы с файлом
 */
public class FileReader {
    /**
     * Прочитать файл и получить список валют предыдущих курсов (не дней)
     *
     * @param csvFilePath путь к файлу
     * @return список строк из файла, содержащие данные по валюте
     * @throws RuntimeException       "Файл не найден"
     * @throws NoSuchElementException "Файл пустой"
     */
    public List<String> readPreviousCourses(String csvFilePath) {
        InputStream inputStream = FileReader.class.getResourceAsStream(csvFilePath);
        if (inputStream == null) {
            throw new RuntimeException(new FileNotFoundException("Файл не найден: " + csvFilePath));
        }
        try (Scanner scanner = new Scanner(inputStream)) {
            if (scanner.hasNextLine()) {
                scanner.nextLine(); // пропускаем заголовок
            } else {
                throw new NoSuchElementException("Файл пустой: " + csvFilePath);
            }
            List<String> result = new ArrayList<>();
            while (scanner.hasNextLine()) {
                result.add(scanner.nextLine());
            }
            return result;
        }
    }
}
