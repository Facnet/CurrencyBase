package ru.liga.currencybase.output;

import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import ru.liga.currencybase.entity.Constant;
import ru.liga.currencybase.entity.Currency;
import ru.liga.currencybase.exception.FileSaveException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Класс для построения графиков
 */
@Slf4j
public class CurrencyLineChart {
    public static final double BORDER_CURS = 0.1;

    /**
     * Сохраняем диаграмму как картинку
     *
     * @param currencies список валют
     * @throws FileSaveException "Файл не сохранился"
     */
    public void saveJpeg(List<Currency> currencies) {
        Collections.reverse(currencies);
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        double minCurs = Double.MAX_VALUE;
        double maxCurs = Double.MIN_VALUE;

        for (Currency currency : currencies) {
            double curs = currency.getCurs().doubleValue();
            dataset.addValue(curs, currency.getCurrencyCode().name(), currency.getDate().format(Constant.FORMATTER_FOR_PRINT_DATE));
            if (curs < minCurs) {
                minCurs = curs;
            }
            if (curs > maxCurs) {
                maxCurs = curs;
            }
        }

        JFreeChart chart = createChart(dataset, minCurs, maxCurs);

        File lineChart = new File("line_Chart.jpeg");
        try {
            ChartUtils.saveChartAsJPEG(lineChart, chart, Constant.DEFAULT_IMAGE_WIDTH, Constant.DEFAULT_IMAGE_HEIGHT);
        } catch (IOException e) {
            log.error("При сохранения файла произошла ошибка: " + e);
            throw new FileSaveException("При сохранения файла произошла ошибка");
        }
    }

    /**
     * Создание JFreeChart
     *
     * @param dataset набор данных
     * @param minCurs минимальный курс
     * @param maxCurs максимальный курс
     * @return JFreeChart
     */
    private JFreeChart createChart(DefaultCategoryDataset dataset, double minCurs, double maxCurs) {
        JFreeChart chart = ChartFactory.createLineChart("Курс валюты", null, "Курс",
                dataset, PlotOrientation.VERTICAL, true, true, false);

        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(minCurs - BORDER_CURS, maxCurs + BORDER_CURS);

        return chart;
    }
}
