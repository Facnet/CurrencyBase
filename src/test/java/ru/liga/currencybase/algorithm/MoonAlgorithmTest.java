package ru.liga.currencybase.algorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.currencybase.entity.*;
import ru.liga.currencybase.exception.InsufficientDataException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

        BigDecimal bigDecimal = new BigDecimal("33.14");
        int j = 1;

        List<Currency> inputCurrencies = new ArrayList<>();
        for (double i = 1; i < Constant.NUMBER_OF_PREVIOUS_COURSES_MONTH + 1; i++, j++) {
            if (i % 2 == 0) {
                bigDecimal = bigDecimal.subtract(BigDecimal.valueOf(i / 10));
            } else {
                bigDecimal = bigDecimal.add(BigDecimal.valueOf(i / 10));
            }
            inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(j), bigDecimal));
        }

        List<Currency> expectedCurrencies = new ArrayList<>();
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(7), new BigDecimal("33.26")));//33,26013348
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(6), new BigDecimal("33.25")));//33,25479422
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(5), new BigDecimal("33.25")));//33,24945495
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(4), new BigDecimal("33.24")));//33,24411568
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(3), new BigDecimal("33.24")));//33,23877642
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(2), new BigDecimal("33.23")));//33,23343715
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(1), new BigDecimal("33.23")));//33,22809789

        List<Currency> actualCurrencies = moonAlgorithm.execute(currencyCode, operationPeriod, inputCurrencies);

        assertEquals(expectedCurrencies, actualCurrencies);
    }

    @Test
    void when_WithValidInput_WithValidDate_shouldReturnCorrectResult() {
        CurrencyCode currencyCode = CurrencyCode.EUR;
        Operation operationDate = new Operation(LocalDate.now().plusDays(1));

        BigDecimal bigDecimal = new BigDecimal("14.14");
        int j = 1;

        List<Currency> inputCurrencies = new ArrayList<>();
        for (double i = 1; i < Constant.NUMBER_OF_PREVIOUS_COURSES_MONTH + 1; i++, j++) {
            if (i % 2 == 0) {
                bigDecimal = bigDecimal.add(BigDecimal.valueOf(i / 10));
            } else {
                bigDecimal = bigDecimal.subtract(BigDecimal.valueOf(i / 10));
            }
            inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(j), bigDecimal));
        }

        List<Currency> expectedCurrencies = new ArrayList<>();
        expectedCurrencies.add(new Currency(currencyCode, operationDate.getDate(), new BigDecimal("14.05")));//14,05190211

        List<Currency> actualCurrencies = moonAlgorithm.execute(currencyCode, operationDate, inputCurrencies);

        assertEquals(expectedCurrencies, actualCurrencies);
    }

    @Test
    void when_WithValidInput_WithNoValidDate_shouldThrowIllegalArgumentException() {
        CurrencyCode currencyCode = CurrencyCode.AMD;
        Operation operationDate = new Operation(LocalDate.now().minusDays(1));

        BigDecimal bigDecimal = new BigDecimal("77.14");
        int j = 1;

        List<Currency> inputCurrencies = new ArrayList<>();
        for (double i = 1; i < Constant.NUMBER_OF_PREVIOUS_COURSES_MONTH + 1; i++, j++) {
            if (i % 2 == 0) {
                bigDecimal = bigDecimal.add(BigDecimal.valueOf(i / 10));
            }
            inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(j), bigDecimal));
        }

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
}