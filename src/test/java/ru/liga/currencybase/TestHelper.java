package ru.liga.currencybase;

import ru.liga.currencybase.entity.Currency;
import ru.liga.currencybase.entity.CurrencyCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestHelper {

    public static List<Currency> receiveCurrency(CurrencyCode currencyCode, BigDecimal beginCurs, int count) {
        int j = 1;
        List<Currency> result = new ArrayList<>();
        for (double i = 1; i < count + 1; i++, j++) {
            if (i % 2 == 0) {
                beginCurs = beginCurs.subtract(BigDecimal.valueOf(i / 10));
            } else {
                beginCurs = beginCurs.add(BigDecimal.valueOf(i / 10));
            }
            result.add(new Currency(currencyCode, LocalDate.now().minusYears(1).minusDays(j), beginCurs));
        }
        return result;
    }
}
