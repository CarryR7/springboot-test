package com.focus.springboottest.service;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.focus.springboottest.entity.PriceInfo;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PriceChartApp implements Runnable {

    public static final Proxy PROXY = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7897));
    private static final String PD_URL = "https://fapi.binance.com";
    public static final String CETUSUSDT = "EPTUSDT";
    public static final String BTCUSDT = "BTCUSDT";
    public static final String SUIUSDT = "SUIUSDT";
    public static final String FORTHUSDT = "FORTHUSDT";

    // 颜色常量
    private static final String RED = "\033[31m";
    private static final String GREEN = "\033[32m";
    private static final String RESET = "\033[0m";

    // 存储上一次的价格
    private Map<String, Double> lastPrices = new HashMap<>();

    private List<Double> xData = new ArrayList<>();
    private List<Double> yData = new ArrayList<>();
    private int time = 0;
    private XYChart chart;
    private SwingWrapper<XYChart> swingWrapper;

    private static final Queue<PriceInfo> priceInfoQueue = new ArrayDeque<>();


    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    public static void main(String[] args) {
        new PriceChartApp().run();
    }



    public void start() {
//        chart = new XYChartBuilder().width(800).height(600).title("Price over Time").xAxisTitle("Time (s)").yAxisTitle("Price").build();
//        chart.getStyler().setMarkerSize(8);
//
//        // Initialize xData and yData with default values
//        xData.add(0.0);
//        yData.add(0.0);
//
//        // Add the series to the chart
//        chart.addSeries(CETUSUSDT, xData, yData).setMarker(SeriesMarkers.NONE);
//
//        // Create the SwingWrapper instance once
//        swingWrapper = new SwingWrapper<>(chart);
//        swingWrapper.displayChart();

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::fetchPrice, 0, 3, TimeUnit.SECONDS);
    }


    private void fetchPrice() {
        JSONObject entries = querySinglePrice(BTCUSDT);
        JSONObject suiusdt = querySinglePrice(SUIUSDT);
        JSONObject entries1 = querySinglePrice(CETUSUSDT);

        // 获取当前价格
        double btcPrice = entries.getDouble("price");
        double suiPrice = suiusdt.getDouble("price");
        double cetusPrice = entries1.getDouble("price");

        // 比较并选择颜色
        String btcColor = getColor(BTCUSDT, btcPrice);
        String suiColor = getColor(SUIUSDT, suiPrice);
        String cetusColor = getColor(CETUSUSDT, cetusPrice);

        System.out.println(String.format("%s\t%s\t%s%s%s\t\t%s\t%s\t%s%s%s\t\t%s\t%s\t%s%s%s",
                entries.getStr("symbol"),
                DATE_FORMAT.format(entries.getDate("time")),
                btcColor, entries.getStr("price"), RESET,
                suiusdt.getStr("symbol"),
                DATE_FORMAT.format(suiusdt.getDate("time")),
                suiColor, suiusdt.getStr("price"), RESET,
                entries1.getStr("symbol"),
                DATE_FORMAT.format(entries1.getDate("time")),
                cetusColor, entries1.getStr("price"), RESET));

        // 更新上一次的价格
        lastPrices.put(BTCUSDT, btcPrice);
        lastPrices.put(SUIUSDT, suiPrice);
        lastPrices.put(CETUSUSDT, cetusPrice);
    }

    // 比较价格并返回对应的颜色
    private String getColor(String symbol, double currentPrice) {
        if (!lastPrices.containsKey(symbol)) {
            return RESET; // 第一次获取，默认颜色
        }
        double lastPrice = lastPrices.get(symbol);
        if (currentPrice > lastPrice) {
            return GREEN; // 上涨，绿色
        } else if (currentPrice < lastPrice) {
            return RED;   // 下跌，红色
        } else {
            return RESET; // 价格不变，默认颜色（或可设为黄色 "\033[33m"）
        }
    }

    public static JSONObject querySinglePrice(String symbol) {
        try {
            URL url = new URL(PD_URL + "/fapi/v1/ticker/price?symbol=" + symbol);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(PROXY);
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            String response = content.toString();
            JSONObject entries = JSONUtil.parseObj(response);
            return entries;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() {
        new PriceChartApp().start();
    }
}