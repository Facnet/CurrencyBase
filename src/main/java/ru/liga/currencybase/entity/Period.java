package ru.liga.currencybase.entity;

public enum Period {
    WEEK(7), TOMORROW(1), MONTH(30);
    private final int dayInPeriod;

    Period(int dayInPeriod) {
        this.dayInPeriod = dayInPeriod;
    }

    public int getDayInPeriod() {
        return dayInPeriod;
    }
}
