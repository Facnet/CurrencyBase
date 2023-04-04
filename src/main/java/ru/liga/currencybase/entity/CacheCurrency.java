package ru.liga.currencybase.entity;

import lombok.extern.slf4j.Slf4j;
import ru.liga.currencybase.file.FileParser;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для работы с валютой
 * Кэш валюты
 */
@Slf4j
public class CacheCurrency {
    private final FileParser fileParser;
    private final Map<CurrencyCode, List<Currency>> cache;
    private final Duration cacheTime;
    private LocalDateTime lastAccessTime;

    public CacheCurrency(FileParser fileParser) {
        this.fileParser = fileParser;
        this.cache = new HashMap<>();
        this.cacheTime = Constant.DEFAULT_CACHE_TIME;
        this.lastAccessTime = LocalDateTime.now();
    }

    /**
     * Возвращаем список валют из кэша
     *
     * @param currencyCode код валюты
     * @return список валют
     */
    public List<Currency> receiveCurrency(CurrencyCode currencyCode) {
        clearCache();
        if (!cache.containsKey(currencyCode)) {
            List<Currency> currencies = fileParser.parseCurrencyFromFile(currencyCode.name());
            cache.put(currencyCode, currencies);
            log.debug("добавили в кэш");
        }
        log.debug("вернули из кэша: " + currencyCode);
        return List.copyOf(cache.get(currencyCode));
    }

    /**
     * Очищаем кеш, каждые Constant.DEFAULT_CACHE_TIME
     */
    private void clearCache() {
        LocalDateTime now = LocalDateTime.now();
        if (lastAccessTime.plus(cacheTime).isBefore(now)) {
            cache.clear();
            lastAccessTime = now;
            log.debug("почистили кэш");
        }
    }
}
