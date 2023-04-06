package ru.liga.currencybase.algorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.currencybase.entity.*;
import ru.liga.currencybase.exception.InsufficientDataException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PastYearAlgorithmTest {

    PastYearAlgorithm pastYearAlgorithm;

    @BeforeEach
    void setUp() {
        pastYearAlgorithm = new PastYearAlgorithm();
    }

    @Test
    void when_WithValidDate_shouldNoException() {
        LocalDate validDate = LocalDate.now().minusDays(1);
        pastYearAlgorithm.checkOperationDate(validDate);
    }

    @Test
    void when_WithNoValidDate_shouldThrowIllegalArgumentException() {
        LocalDate pastDate = LocalDate.parse("05.10.2000", Constant.FORMATTER_FOR_PARSE_FILE);
        assertThatThrownBy(() -> pastYearAlgorithm.checkOperationDate(pastDate))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_WithValidInput_WithPeriod_shouldThrowReturnCorrectResult() {
        CurrencyCode currencyCode = CurrencyCode.EUR;
        Operation operationPeriod = new Operation(Period.WEEK);

        List<Currency> inputCurrencies = new ArrayList<>();
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(1).minusYears(1), new BigDecimal("111.14")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(2).minusYears(1), new BigDecimal("222.14")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(3).minusYears(1), new BigDecimal("333.14")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(4).minusYears(1), new BigDecimal("444.14")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(5).minusYears(1), new BigDecimal("555.14")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(6).minusYears(1), new BigDecimal("666.14")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(7).minusYears(1), new BigDecimal("777.14")));

        List<Currency> expectedCurrencies = new ArrayList<>(inputCurrencies);
        Collections.reverse(expectedCurrencies);

        List<Currency> actualCurrencies = pastYearAlgorithm.execute(currencyCode, operationPeriod, inputCurrencies);

        assertEquals(expectedCurrencies, actualCurrencies);

    }

    @Test
    void when_WithNoValidInput_WithValidDate_shouldThrowInsufficientDataException() {
        CurrencyCode currencyCode = CurrencyCode.TRY;
        Operation operationDate = new Operation(LocalDate.now().minusDays(4));

        List<Currency> inputCurrencies = new ArrayList<>();
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(1), new BigDecimal("666.14")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(2), new BigDecimal("777.14")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(3), new BigDecimal("555.14")));

        assertThatThrownBy(() -> pastYearAlgorithm.execute(currencyCode, operationDate, inputCurrencies))
                .isExactlyInstanceOf(InsufficientDataException.class);
    }

    @Test
    void when_WithValidInput_WithValidDate_shouldReturnCorrectResult() {
        CurrencyCode currencyCode = CurrencyCode.AMD;
        Operation operationDate = new Operation(LocalDate.now().minusDays(2));

        List<Currency> inputCurrencies = new ArrayList<>();
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(1), new BigDecimal("666.14")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(2).minusYears(1), new BigDecimal("777.14")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(3), new BigDecimal("555.14")));

        assertThat(pastYearAlgorithm.execute(currencyCode, operationDate, inputCurrencies))
                .hasSize(1)
                .containsExactly(new Currency(currencyCode, operationDate.getDate().minusYears(1), new BigDecimal("777.14")));

    }
}