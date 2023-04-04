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
import static org.junit.jupiter.api.Assertions.assertEquals;

class OldAlgorithmTest {

    OldAlgorithm oldAlgorithm;

    @BeforeEach
    void setUp() {
        oldAlgorithm = new OldAlgorithm();
    }

    @Test
    void when_WithValidInput_WithPeriod_shouldReturnCorrectResult() {
        CurrencyCode currencyCode = CurrencyCode.EUR;
        Operation operationPeriod = new Operation(Period.WEEK);

        List<Currency> inputCurrencies = new ArrayList<>();
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(1), new BigDecimal("33.14")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(2), new BigDecimal("33.15")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(3), new BigDecimal("33.16")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(4), new BigDecimal("33.17")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(5), new BigDecimal("33.18")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(6), new BigDecimal("33.19")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(7), new BigDecimal("33.20")));

        List<Currency> expectedCurrencies = new ArrayList<>();
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(7), new BigDecimal("33.16")));//33,15912749
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(6), new BigDecimal("33.16")));//33,15798655
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(5), new BigDecimal("33.16")));//33,15823823
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(4), new BigDecimal("33.16")));//33,15970845
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(3), new BigDecimal("33.16")));//33,1622449
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(2), new BigDecimal("33.17")));//33,16571429
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(1), new BigDecimal("33.17")));//33,17

        List<Currency> actualCurrencies = oldAlgorithm.execute(currencyCode, operationPeriod, inputCurrencies);

        assertEquals(expectedCurrencies, actualCurrencies);
    }


    @Test
    void when_WithValidInput_WithValidDate_shouldReturnCorrectResult() {
        CurrencyCode currencyCode = CurrencyCode.AMD;
        Operation operationDate = new Operation(LocalDate.now().plusDays(1));

        List<Currency> inputCurrencies = new ArrayList<>();
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(1), new BigDecimal("50.6")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(2), new BigDecimal("55.5")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(3), new BigDecimal("52.6")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(4), new BigDecimal("52.7")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(5), new BigDecimal("53.4")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(6), new BigDecimal("52.4")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(7), new BigDecimal("51.1")));

        List<Currency> expectedCurrencies = new ArrayList<>();
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(1), new BigDecimal("52.61")));//52,61428571

        List<Currency> actualCurrencies = oldAlgorithm.execute(currencyCode, operationDate, inputCurrencies);

        assertEquals(expectedCurrencies, actualCurrencies);
    }

    @Test
    void when_WithNoValidInput_shouldThrowInsufficientDataException() {
        CurrencyCode currencyCode = CurrencyCode.TRY;
        Operation operationPeriod = new Operation(Period.TOMORROW);
        Operation operationDate = new Operation(LocalDate.now().plusDays(1));

        List<Currency> inputCurrencies = new ArrayList<>();
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(1), new BigDecimal("33.14")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(2), new BigDecimal("33.15")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(3), new BigDecimal("33.16")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(4), new BigDecimal("33.17")));

        assertThatThrownBy(() -> oldAlgorithm.execute(currencyCode, operationPeriod, inputCurrencies))
                .isExactlyInstanceOf(InsufficientDataException.class);

        assertThatThrownBy(() -> oldAlgorithm.execute(currencyCode, operationDate, inputCurrencies))
                .isExactlyInstanceOf(InsufficientDataException.class);
    }

    @Test
    void when_WithValidInput_WithNoValidDate_shouldThrowIllegalArgumentException() {
        CurrencyCode currencyCode = CurrencyCode.BGN;
        Operation operationDate = new Operation(LocalDate.now());

        List<Currency> inputCurrencies = new ArrayList<>();
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(1), new BigDecimal("50.6")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(2), new BigDecimal("55.5")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(3), new BigDecimal("52.6")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(4), new BigDecimal("52.7")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(5), new BigDecimal("53.4")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(6), new BigDecimal("52.4")));
        inputCurrencies.add(new Currency(currencyCode, LocalDate.now().minusDays(7), new BigDecimal("51.1")));

        assertThatThrownBy(() -> oldAlgorithm.execute(currencyCode, operationDate, inputCurrencies))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}