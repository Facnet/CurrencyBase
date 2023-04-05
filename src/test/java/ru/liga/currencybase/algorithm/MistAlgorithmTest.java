package ru.liga.currencybase.algorithm;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.currencybase.entity.Currency;
import ru.liga.currencybase.entity.CurrencyCode;
import ru.liga.currencybase.entity.Operation;
import ru.liga.currencybase.entity.Period;
import ru.liga.currencybase.exception.InsufficientDataException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class MistAlgorithmTest {

    MistAlgorithm mistAlgorithm;

    @BeforeEach
    void setUp() {
        mistAlgorithm = new MistAlgorithm();
    }

    @Test
    void when_WithValidInput_WithPeriod_shouldReturnCorrectResult() {
        CurrencyCode currencyCode = CurrencyCode.EUR;
        Operation operationPeriod = new Operation(Period.TOMORROW);

        List<Currency> inputCurrencies = new ArrayList<>();
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(1), new BigDecimal("50.6")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(2), new BigDecimal("55.5")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(3), new BigDecimal("52.6")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(1), new BigDecimal("52.7")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(5), new BigDecimal("53.4")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(6), new BigDecimal("52.4")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(7), new BigDecimal("51.1")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(8), new BigDecimal("51.1")));

        List<Currency> expectedCurrencies = new ArrayList<>();
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(1), new BigDecimal("52.70")));

        List<Currency> actualCurrencies = mistAlgorithm.execute(currencyCode, operationPeriod, inputCurrencies);

        assertEquals(expectedCurrencies, actualCurrencies);
    }

    @Test
    void when_WithValidInput_WithValidDate_shouldReturnCorrectResult() {
        CurrencyCode currencyCode = CurrencyCode.EUR;
        Operation operationDate = new Operation(LocalDate.now().plusDays(3));

        List<Currency> inputCurrencies = new ArrayList<>();
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(1), new BigDecimal("78.6")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(2), new BigDecimal("54.5")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(3), new BigDecimal("654.6")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(4), new BigDecimal("65.7")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(5), new BigDecimal("4.4")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(6), new BigDecimal("552.4")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(7), new BigDecimal("561.1")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(8), new BigDecimal("541.1")));

        List<Currency> expectedCurrencies = new ArrayList<>();
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(3), new BigDecimal("654.60")));

        List<Currency> actualCurrencies = mistAlgorithm.execute(currencyCode, operationDate, inputCurrencies);

        assertEquals(expectedCurrencies, actualCurrencies);
    }

    @Test
    void when_WithValidInput_WithNoValidDate_shouldThrowIllegalArgumentException() {
        CurrencyCode currencyCode = CurrencyCode.EUR;
        Operation operationDate = new Operation(LocalDate.now().minusDays(3));

        List<Currency> inputCurrencies = new ArrayList<>();
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(1), new BigDecimal("78.6")));

        assertThatThrownBy(() -> mistAlgorithm.execute(currencyCode, operationDate, inputCurrencies))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_WithNoValidInput_shouldThrowInsufficientDataException() {
        CurrencyCode currencyCode = CurrencyCode.EUR;
        Operation operationPeriod = new Operation(Period.WEEK);
        Operation operationDate = new Operation(LocalDate.now().plusDays(1));

        List<Currency> inputCurrencies = new ArrayList<>();
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(10), new BigDecimal("55.54")));

        assertThatThrownBy(() -> mistAlgorithm.execute(currencyCode, operationPeriod, inputCurrencies))
                .isExactlyInstanceOf(InsufficientDataException.class);

        assertThatThrownBy(() -> mistAlgorithm.execute(currencyCode, operationDate, inputCurrencies))
                .isExactlyInstanceOf(InsufficientDataException.class);
    }
}