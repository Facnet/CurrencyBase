package ru.liga.currencybase.executor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.liga.currencybase.entity.*;
import ru.liga.currencybase.exception.InsufficientDataException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static ru.liga.currencybase.TestHelper.receiveCurrency;

class AlgorithmExecutorTest {

    private CacheCurrency cacheCurrency;
    private AlgorithmExecutor algorithmExecutor;

    @BeforeEach
    void setUp() {
        cacheCurrency = Mockito.mock(CacheCurrency.class);
        algorithmExecutor = new AlgorithmExecutor(cacheCurrency);
    }

    @Test
    void when_WithValidInput_WithPeriod_shouldReturnCorrectResult() {
        CurrencyCode currencyCode = CurrencyCode.EUR;
        Operation operationPeriod = new Operation(Period.WEEK);

        when(cacheCurrency.receiveCurrency(currencyCode)).thenReturn(
                receiveCurrency(currencyCode, new BigDecimal("70.70"), Constant.NUMBER_OF_PREVIOUS_COURSES_MONTH)
        );
        assertThat(algorithmExecutor.executeAlgorithm(Algorithm.OLD, currencyCode, operationPeriod)).hasSize(7);

        when(cacheCurrency.receiveCurrency(currencyCode)).thenReturn(
                receiveCurrency(currencyCode, new BigDecimal("60.60"), Constant.NUMBER_OF_PREVIOUS_COURSES_MONTH)
        );
        assertThat(algorithmExecutor.executeAlgorithm(Algorithm.MOON, currencyCode, operationPeriod)).hasSize(7);

        when(cacheCurrency.receiveCurrency(currencyCode)).thenReturn(
                receiveCurrency(currencyCode, new BigDecimal("44.56"), Constant.NUMBER_OF_PREVIOUS_COURSES_MONTH)
        );
        assertThat(algorithmExecutor.executeAlgorithm(Algorithm.MIST, currencyCode, operationPeriod)).hasSize(7);

        when(cacheCurrency.receiveCurrency(currencyCode)).thenReturn(
                receiveCurrency(currencyCode, new BigDecimal("12.32"), Constant.NUMBER_OF_PREVIOUS_COURSES_MONTH)
        );
        assertThat(algorithmExecutor.executeAlgorithm(Algorithm.PAST_YEAR, currencyCode, operationPeriod)).hasSize(7);
    }

    @Test
    void when_WithNoValidInput_WithPeriod_shouldThrowInsufficientDataException() {
        CurrencyCode currencyCode = CurrencyCode.USD;
        Operation operationPeriod = new Operation(Period.WEEK);
        when(cacheCurrency.receiveCurrency(currencyCode)).thenReturn(List.of());

        assertThatThrownBy(() -> algorithmExecutor.executeAlgorithm(Algorithm.OLD, currencyCode, operationPeriod))
                .isExactlyInstanceOf(InsufficientDataException.class);

        assertThatThrownBy(() -> algorithmExecutor.executeAlgorithm(Algorithm.MOON, currencyCode, operationPeriod))
                .isExactlyInstanceOf(InsufficientDataException.class);

        assertThatThrownBy(() -> algorithmExecutor.executeAlgorithm(Algorithm.MIST, currencyCode, operationPeriod))
                .isExactlyInstanceOf(InsufficientDataException.class);

        assertThatThrownBy(() -> algorithmExecutor.executeAlgorithm(Algorithm.PAST_YEAR, currencyCode, operationPeriod))
                .isExactlyInstanceOf(InsufficientDataException.class);
    }

    @Test
    void when_WithValidInput_WithValidDate_shouldReturnCorrectResult() {
        CurrencyCode currencyCode = CurrencyCode.TRY;
        Operation operationDate = new Operation(LocalDate.now().plusDays(1));

        when(cacheCurrency.receiveCurrency(currencyCode)).thenReturn(
                receiveCurrency(currencyCode, new BigDecimal("99.70"), Constant.NUMBER_OF_PREVIOUS_COURSES_MONTH)
        );
        assertThat(algorithmExecutor.executeAlgorithm(Algorithm.OLD, currencyCode, operationDate)).hasSize(1);

        when(cacheCurrency.receiveCurrency(currencyCode)).thenReturn(
                receiveCurrency(currencyCode, new BigDecimal("23.60"), Constant.NUMBER_OF_PREVIOUS_COURSES_MONTH)
        );
        assertThat(algorithmExecutor.executeAlgorithm(Algorithm.MOON, currencyCode, operationDate)).hasSize(1);

        when(cacheCurrency.receiveCurrency(currencyCode)).thenReturn(
                receiveCurrency(currencyCode, new BigDecimal("45.56"), Constant.NUMBER_OF_PREVIOUS_COURSES_MONTH)
        );
        assertThat(algorithmExecutor.executeAlgorithm(Algorithm.MIST, currencyCode, operationDate)).hasSize(1);

        when(cacheCurrency.receiveCurrency(currencyCode)).thenReturn(
                receiveCurrency(currencyCode, new BigDecimal("89.32"), Constant.NUMBER_OF_PREVIOUS_COURSES_MONTH)
        );
        assertThat(algorithmExecutor.executeAlgorithm(Algorithm.PAST_YEAR, currencyCode, operationDate)).hasSize(1);
    }

    @Test
    void when_WithNoValidInput_WithValidDate_shouldThrowInsufficientDataException() {
        CurrencyCode currencyCode = CurrencyCode.AMD;
        Operation operationDate = new Operation(LocalDate.now().plusDays(1));
        when(cacheCurrency.receiveCurrency(currencyCode)).thenReturn(List.of());

        assertThatThrownBy(() -> algorithmExecutor.executeAlgorithm(Algorithm.OLD, currencyCode, operationDate))
                .isExactlyInstanceOf(InsufficientDataException.class);

        assertThatThrownBy(() -> algorithmExecutor.executeAlgorithm(Algorithm.MOON, currencyCode, operationDate))
                .isExactlyInstanceOf(InsufficientDataException.class);

        assertThatThrownBy(() -> algorithmExecutor.executeAlgorithm(Algorithm.MIST, currencyCode, operationDate))
                .isExactlyInstanceOf(InsufficientDataException.class);

        assertThatThrownBy(() -> algorithmExecutor.executeAlgorithm(Algorithm.PAST_YEAR, currencyCode, operationDate))
                .isExactlyInstanceOf(InsufficientDataException.class);
    }

}