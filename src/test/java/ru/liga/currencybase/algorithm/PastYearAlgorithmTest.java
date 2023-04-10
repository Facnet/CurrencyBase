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
import static ru.liga.currencybase.TestHelper.receiveCurrency;

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
    void when_WithValidInput_WithPeriod_shouldReturnCorrectResult() {
        CurrencyCode currencyCode = CurrencyCode.EUR;
        Operation operationPeriod = new Operation(Period.TOMORROW);

        List<Currency> inputCurrencies = new ArrayList<>(
                receiveCurrency(currencyCode, new BigDecimal("444.14"), Constant.NUMBER_OF_PREVIOUS_COURSES_WEEK)
        );

        List<Currency> expectedCurrencies = Collections.singletonList(inputCurrencies.get(0));

        List<Currency> actualCurrencies = pastYearAlgorithm.execute(currencyCode, operationPeriod, inputCurrencies);

        assertEquals(expectedCurrencies, actualCurrencies);

    }

    @Test
    void when_WithValidInput_WithValidDate_shouldReturnCorrectResult() {
        CurrencyCode currencyCode = CurrencyCode.AMD;
        Operation operationDate = new Operation(LocalDate.now().minusDays(10));

        List<Currency> inputCurrencies = new ArrayList<>(
                receiveCurrency(currencyCode, new BigDecimal("22.22"), Constant.NUMBER_OF_PREVIOUS_COURSES_WEEK)
        );
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusYears(1).minusDays(10), new BigDecimal("777.14")));

        assertThat(pastYearAlgorithm.execute(currencyCode, operationDate, inputCurrencies))
                .hasSize(1)
                .containsExactly(new Currency(currencyCode, operationDate.getDate().minusYears(1), new BigDecimal("777.14")));

    }

    @Test
    void when_WithNoValidInput_shouldThrowInsufficientDataException() {
        CurrencyCode currencyCode = CurrencyCode.TRY;
        Operation operationDate = new Operation(LocalDate.now().minusDays(4));
        Operation operationPeriod = new Operation(Period.WEEK);

        List<Currency> inputCurrencies = new ArrayList<>();
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(1), new BigDecimal("666.14")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(2), new BigDecimal("777.14")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(3), new BigDecimal("555.14")));

        assertThatThrownBy(() -> pastYearAlgorithm.execute(currencyCode, operationDate, inputCurrencies))
                .isExactlyInstanceOf(InsufficientDataException.class);

        assertThatThrownBy(() -> pastYearAlgorithm.execute(currencyCode, operationPeriod, inputCurrencies))
                .isExactlyInstanceOf(InsufficientDataException.class);
    }
}