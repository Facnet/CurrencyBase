package ru.liga.currencybase.algorithm;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.currencybase.entity.*;
import ru.liga.currencybase.exception.InsufficientDataException;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.liga.currencybase.TestHelper.receiveCurrency;

/**
 * Перед запуском теста открыть и сохранить файл src/test/resources/test.xlsx
 */
class MoonAlgorithmTest {

    MoonAlgorithm moonAlgorithm;

    @BeforeEach
    void setUp() {
        moonAlgorithm = new MoonAlgorithm();
    }

    @Test
    void when_WithValidInput_WithPeriod_shouldReturnCorrectResult() {
        CurrencyCode currencyCode = CurrencyCode.EUR;
        Operation operationPeriod = new Operation(Period.WEEK);

        List<Currency> inputCurrencies = new ArrayList<>(
                receiveCurrency(currencyCode, new BigDecimal("33.14"), Constant.NUMBER_OF_PREVIOUS_COURSES_MONTH)
        );

        String[] values = getValuesFromXlsx();
        List<Currency> expectedCurrencies = new ArrayList<>();
        for (int i = 0; i < Constant.NUMBER_OF_PREVIOUS_COURSES_WEEK; i++) {
            expectedCurrencies.add(0,
                    new Currency(currencyCode, LocalDate.now().plusDays(i + 1), new BigDecimal(values[i]).setScale(2, RoundingMode.HALF_UP)));
        }

        List<Currency> actualCurrencies = moonAlgorithm.execute(currencyCode, operationPeriod, inputCurrencies);

        assertEquals(expectedCurrencies, actualCurrencies);
    }

    @Test
    void when_WithValidInput_WithValidDate_shouldReturnCorrectResult() {
        CurrencyCode currencyCode = CurrencyCode.EUR;
        Operation operationDate = new Operation(LocalDate.now().plusDays(1));

        List<Currency> inputCurrencies = new ArrayList<>(
                receiveCurrency(currencyCode, new BigDecimal("14.14"), Constant.NUMBER_OF_PREVIOUS_COURSES_MONTH)
        );

        List<Currency> expectedCurrencies = new ArrayList<>();
        expectedCurrencies.add(new Currency(currencyCode, operationDate.getDate(), new BigDecimal(getValueFromXlsx())));

        List<Currency> actualCurrencies = moonAlgorithm.execute(currencyCode, operationDate, inputCurrencies);

        assertEquals(expectedCurrencies, actualCurrencies);
    }

    @Test
    void when_WithValidInput_WithNoValidDate_shouldThrowIllegalArgumentException() {
        CurrencyCode currencyCode = CurrencyCode.AMD;
        Operation operationDate = new Operation(LocalDate.now().minusDays(1));

        List<Currency> inputCurrencies = new ArrayList<>(
                receiveCurrency(currencyCode, new BigDecimal("77.14"),Constant.NUMBER_OF_PREVIOUS_COURSES_MONTH)
        );

        assertThatThrownBy(() -> moonAlgorithm.execute(currencyCode, operationDate, inputCurrencies))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_WithNoValidInput_shouldThrowInsufficientDataException() {
        CurrencyCode currencyCode = CurrencyCode.TRY;
        Operation operationPeriod = new Operation(Period.WEEK);
        Operation operationDate = new Operation(LocalDate.now().plusDays(1));

        BigDecimal bigDecimal = new BigDecimal("15.14");

        List<Currency> inputCurrencies = new ArrayList<>();
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(1), bigDecimal));

        assertThatThrownBy(() -> moonAlgorithm.execute(currencyCode, operationPeriod, inputCurrencies))
                .isExactlyInstanceOf(InsufficientDataException.class);

        assertThatThrownBy(() -> moonAlgorithm.execute(currencyCode, operationDate, inputCurrencies))
                .isExactlyInstanceOf(InsufficientDataException.class);
    }

    private String getValueFromXlsx() {
        String result = "";
        try {
            FileInputStream file = new FileInputStream("src/test/resources/test.xlsx");
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(1); // Получить второй лист
            Row row = sheet.getRow(32); // Получить 32-ю строку (нумерация с 0)
            Cell cell = row.getCell(5); // Получить ячейку F33
            result = String.valueOf(cell.getNumericCellValue()); // Вывести значение ячейки
            workbook.close(); // Закрыть файл
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String[] getValuesFromXlsx() {
        String[] result = new String[7];
        try {
            FileInputStream file = new FileInputStream("src/test/resources/test.xlsx");
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(2); // Получить третий лист
            for (int i = 0; i < Constant.NUMBER_OF_PREVIOUS_COURSES_WEEK; i++) {
                Row row = sheet.getRow(32 + i); // Получить 32-ю строку (нумерация с 0)
                Cell cell = row.getCell(5); // Получить ячейку F33 - F39
                result[i] = String.valueOf(cell.getNumericCellValue()); // Вывести значение ячейки
            }
            workbook.close(); // Закрыть файл
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}