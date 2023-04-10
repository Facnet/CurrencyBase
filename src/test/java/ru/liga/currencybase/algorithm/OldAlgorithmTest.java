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
import static ru.liga.currencybase.TestHelper.receiveCurrency;

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

        List<Currency> inputCurrencies = new ArrayList<>(
                receiveCurrency(currencyCode, new BigDecimal("66.94"), Constant.NUMBER_OF_PREVIOUS_COURSES_WEEK)
        );

        List<Currency> expectedCurrencies = new ArrayList<>();
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(7), new BigDecimal("66.99")));//66,98480089
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(6), new BigDecimal("66.97")));//66,96670078
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(5), new BigDecimal("66.99")));//66,98836318
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(4), new BigDecimal("66.96")));//66,95731778
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(3), new BigDecimal("66.99")));//66,99265306
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(2), new BigDecimal("66.95")));//66,94857143
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(1), new BigDecimal("67.00")));//67

        List<Currency> actualCurrencies = oldAlgorithm.execute(currencyCode, operationPeriod, inputCurrencies);

        assertEquals(expectedCurrencies, actualCurrencies);
    }


    @Test
    void when_WithValidInput_WithValidDate_shouldReturnCorrectResult() {
        CurrencyCode currencyCode = CurrencyCode.AMD;
        Operation operationDate = new Operation(LocalDate.now().plusDays(1));

        List<Currency> inputCurrencies = new ArrayList<>(
                receiveCurrency(currencyCode, new BigDecimal("50.60"), Constant.NUMBER_OF_PREVIOUS_COURSES_WEEK)
        );

        List<Currency> expectedCurrencies = new ArrayList<>();
        expectedCurrencies.add(new Currency(currencyCode, LocalDate.now().plusDays(1), new BigDecimal("50.66")));//50,65714286

        List<Currency> actualCurrencies = oldAlgorithm.execute(currencyCode, operationDate, inputCurrencies);

        assertEquals(expectedCurrencies, actualCurrencies);
    }

    @Test
    void when_WithValidInput_WithNoValidDate_shouldThrowIllegalArgumentException() {
        CurrencyCode currencyCode = CurrencyCode.BGN;
        Operation operationDate = new Operation(LocalDate.now());

        List<Currency> inputCurrencies = new ArrayList<>(
                receiveCurrency(currencyCode, new BigDecimal("75.55"), Constant.NUMBER_OF_PREVIOUS_COURSES_WEEK)
        );

        assertThatThrownBy(() -> oldAlgorithm.execute(currencyCode, operationDate, inputCurrencies))
                .isExactlyInstanceOf(IllegalArgumentException.class);
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
}