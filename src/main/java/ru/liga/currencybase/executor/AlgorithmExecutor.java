package ru.liga.currencybase.executor;

import ru.liga.currencybase.algorithm.*;
import ru.liga.currencybase.entity.Algorithm;
import ru.liga.currencybase.entity.CurrencyCode;
import ru.liga.currencybase.entity.Operation;
import ru.liga.currencybase.entity.CacheCurrency;
import ru.liga.currencybase.entity.Currency;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для запуска алгоритма
 */
public class AlgorithmExecutor {
    private final CacheCurrency cache;

    public AlgorithmExecutor(CacheCurrency cache) {
        this.cache = cache;
    }

    /**
     * Выполняем алгоритм
     *
     * @param algorithm    алгоритм
     * @param currencyCode код валюты
     * @param operation    операция
     * @return список валюты
     * @throws IllegalArgumentException "Некорректный алгоритм"
     */
    public List<Currency> executeAlgorithm(Algorithm algorithm, CurrencyCode currencyCode, Operation operation) {
        List<Currency> currencies = new ArrayList<>(cache.receiveCurrency(currencyCode));
        Algorithms algorithmImpl;
        switch (algorithm) {
            case OLD -> algorithmImpl = new OldAlgorithm();
            case PAST_YEAR -> algorithmImpl = new PastYearAlgorithm();
            case MIST -> algorithmImpl = new MistAlgorithm();
            case MOON -> algorithmImpl = new MoonAlgorithm();
            default -> throw new IllegalArgumentException("Ой, забыли реализовать алгоритм, для " + algorithm);
        }
        return algorithmImpl.execute(currencyCode, operation, currencies);
    }
}