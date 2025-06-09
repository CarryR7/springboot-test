package com.focus.springboottest.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.focus.springboottest.entity.Candlestick;
import com.focus.springboottest.util.CandlestickParser;
import com.focus.springboottest.util.EmailUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Service
public class BinanceCandlestickPrinter {

    public static final String TO = "1106288595@qq.com";
    public static final String SUBJECT = "价格提醒";
    // ANSI颜色代码
    private static final String RED = "\033[31m";
    private static final String GREEN = "\033[32m";
    private static final String RESET = "\033[0m";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm");
    public static final Proxy PROXY = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7897));

    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }


    @Autowired
    private EmailUtil emailUtil;

    /*public static void main(String[] args) {
        String symbol = "CETUSUSDT";
        int limit = 5;
        String interval = "5m";

        try {
            List<Candlestick> klines = getKlines(symbol, interval, limit);
//            printCandlesConnected(klines, 10); // 10行高度的蜡烛图
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public static List<Candlestick> getKlines(String symbol, String interval, int limit) throws Exception {
        String url = String.format("https://fapi.binance.com/fapi/v1/klines?symbol=%s&interval=%s&limit=%d",
                symbol, interval, limit);

        // 配置带代理的OkHttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .proxy(PROXY)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0")
                .build();

        List<Candlestick> candlesticks = new ArrayList<>();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            JSONArray objects = JSON.parseArray(responseBody);
            for (int i = 0; i < objects.size(); i++) {
                JSONArray jsonArray = objects.getJSONArray(i);
                CandlestickParser candlestickParser = new CandlestickParser();
                Candlestick parse = candlestickParser.parse(jsonArray);
                candlesticks.add(parse);
            }
            return candlesticks;
        }
    }


    public static List<Candlestick> getKlinesByTime(String symbol, String interval, Long startTime, Long endTime) throws Exception {
        StringBuilder urlBuilder = new StringBuilder(String.format("https://fapi.binance.com/fapi/v1/klines?symbol=%s&interval=%s", symbol, interval));

        if (startTime != null) {
            urlBuilder.append("&startTime=").append(startTime);
        }
        if (endTime != null) {
            urlBuilder.append("&endTime=").append(endTime);
        }
        urlBuilder.append("&limit=1000");

        OkHttpClient client = new OkHttpClient.Builder()
                .proxy(PROXY)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(urlBuilder.toString())
                .addHeader("User-Agent", "Mozilla/5.0")
                .build();

        List<Candlestick> candlesticks = new ArrayList<>();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            JSONArray objects = JSON.parseArray(responseBody);
            for (int i = 0; i < objects.size(); i++) {
                JSONArray jsonArray = objects.getJSONArray(i);
                CandlestickParser candlestickParser = new CandlestickParser();
                Candlestick parse = candlestickParser.parse(jsonArray);
                candlesticks.add(parse);
            }
        }
        return candlesticks;
    }


    //获取当前价格https://fapi.binance.com/fapi/v1/ticker/price?symbol=SUIUSDT
    public static String getCurrentPrice(String symbol) throws Exception {
        String url = "https://fapi.binance.com/fapi/v1/ticker/price?symbol="+symbol;
        OkHttpClient client = new OkHttpClient.Builder()
                .proxy(PROXY)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("User-Agent", "Mozilla/5.0")
                        .build();
                try (Response response = client.newCall(request).execute()) {
                    String responseBody = response.body().string();
                    //{
                    //    "symbol": "SUIUSDT",
                    //    "price": "3.392600",
                    //    "time": 1746603078845
                    //}
                    JSONObject objects = JSON.parseObject(responseBody, JSONObject.class);
                    return objects.getString("price");
                }
    }


    //实时价格提醒功能
    @Async
    public void realTimePriceReminder(String symbol, String targetPrice, String stopLossPrice) throws Exception {
        while (true) {
            String currentPrice = getCurrentPrice(symbol);
            System.out.println("当前价格：" + currentPrice);
            if (currentPrice != null) {
                BigDecimal currentPriceBigDecimal = new BigDecimal(currentPrice);
                BigDecimal targetPriceBigDecimal = new BigDecimal(targetPrice);
                if (currentPriceBigDecimal.compareTo(targetPriceBigDecimal) >= 0) {
                    System.out.println("目标价格已到达！");

                    String text = symbol + "价格到达" + targetPrice + "，请及时处理！";

                    emailUtil.sendSimpleEmail(TO, SUBJECT, text);

                    break;
                }
            }
            Thread.sleep(1000);


            /*if (currentPrice != null) {
                double currentPriceDouble = Double.parseDouble(currentPrice);
                if (currentPriceDouble <= stopLossPrice) {
                    System.out.println("止损价格已触发！");
                }
            }*/
        }
    }




}