package com.focus.springboottest.service;

import cn.hutool.json.JSONObject;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Second;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.focus.springboottest.service.PriceChartApp.*;

public class RealTimePriceChart extends JFrame {

    private final TimeSeries series;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private final DecimalFormat priceFormat = new DecimalFormat("#,##0.0000000000");

    public RealTimePriceChart() {
        super("实时价格折线图");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // 初始化时间序列数据集
        series = new TimeSeries("价格");
        TimeSeriesCollection dataset = new TimeSeriesCollection(series);

        // 创建图表
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "价格趋势图",
                "时间",
                "价格",
                dataset,
                true,
                true,
                false
        );

        // 获取图表绘图区域
        XYPlot plot = chart.getXYPlot();

        // -------------------- 配置 X 轴 --------------------
        DateAxis xAxis = (DateAxis) plot.getDomainAxis();
        xAxis.setDateFormatOverride(timeFormat); // 时间格式化为 HH:mm:ss

        // -------------------- 配置 Y 轴 --------------------
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setNumberFormatOverride(priceFormat); // 禁用科学计数法，显示为常规数字

        // -------------------- 配置工具提示 --------------------
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        // 自定义提示内容：显示时间和价格
        renderer.setDefaultToolTipGenerator(new StandardXYToolTipGenerator(
                "{0}: {1} -> {2}", // {0}=系列名, {1}=时间, {2}=价格
                timeFormat,
                priceFormat
        ));

        // 设置字体以支持中文
        Font font = new Font("宋体", Font.PLAIN, 12);
        chart.getTitle().setFont(font);
        xAxis.setTickLabelFont(font);
        xAxis.setLabelFont(font);
        yAxis.setTickLabelFont(font);
        yAxis.setLabelFont(font);
        chart.getLegend().setItemFont(font);
        plot.getDomainAxis().setLabelFont(font);
        plot.getRangeAxis().setLabelFont(font);

        // 将图表添加到面板
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);

        // 启动定时任务：每秒添加新数据点
        Timer timer = new Timer(1000, e -> updateChart());
        timer.start();
    }

    private void updateChart() {
        // 模拟生成数据（替换为实际数据源）
        JSONObject entries = querySinglePrice(SUIUSDT);
//        String symbol = entries.getStr("symbol");
        Date time = entries.getDate("time");
        BigDecimal price = entries.getBigDecimal("price");
//        PriceInfo newPrice = new PriceInfo("CETUSUSDT", String.valueOf(price), time);

        // 更新图表
        SwingUtilities.invokeLater(() -> {
            try {
                series.addOrUpdate(new Second(time), price);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RealTimePriceChart chart = new RealTimePriceChart();
            chart.setVisible(true);
        });
    }
}