package ru.liga.currencybase.entity;

import java.time.LocalDate;

/**
 * Класс операции
 */
public class Operation {
    private Period period;
    private LocalDate date;

    public Operation(Period period) {
        this.period = period;
    }

    public Operation(LocalDate date) {
        this.date = date;
    }

    public Period getPeriod() {
        return period;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Operation{" + "period=" + period + ", date=" + date + '}';
    }

    public boolean isNotNullDate(){
        return date != null;
    }

    public boolean isNotNullPeriod(){
        return period != null;
    }

}
