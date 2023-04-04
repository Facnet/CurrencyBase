package ru.liga.currencybase.factory;

import ru.liga.currencybase.entity.CacheCurrency;
import ru.liga.currencybase.executor.AlgorithmExecutor;
import ru.liga.currencybase.executor.OutputExecutor;
import ru.liga.currencybase.executor.RateExecutor;
import ru.liga.currencybase.file.FileParser;
import ru.liga.currencybase.file.FileReader;

/**
 * Класс Factory? для команды RateExecutor
 */
public class RateFactory {

    public static RateExecutor buildRateExecutor() {
        FileReader fileReader = new FileReader();
        FileParser fileParser = new FileParser(fileReader);
        CacheCurrency cacheCurrency = new CacheCurrency(fileParser);

        AlgorithmExecutor algorithmExecutor = new AlgorithmExecutor(cacheCurrency);
        OutputExecutor outputExecutor = new OutputExecutor();

        return new RateExecutor(algorithmExecutor,outputExecutor);
    }
}
