package ru.liga.currencybase.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Класс валюты
 */
public class Currency {
    private final CurrencyCode currencyCode;
    private final LocalDate date;
    private final BigDecimal curs;

    public Currency(CurrencyCode currencyCode, LocalDate date, BigDecimal curs) {
        this.currencyCode = currencyCode;
        this.date = date;
        this.curs = curs;
    }

    public BigDecimal getCurs() {
        return curs;
    }

    public LocalDate getDate() {
        return date;
    }

    public CurrencyCode getCurrencyCode() {
        return currencyCode;
    }

    @Override
    public String toString() {
        return currencyCode + ": " + date.format(Constant.FORMATTER_FOR_PRINT_DATE) + " - " + curs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return date.equals(currency.date) && curs.equals(currency.curs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, curs);
    }
}
