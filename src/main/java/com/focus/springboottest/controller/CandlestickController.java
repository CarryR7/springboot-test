package com.focus.springboottest.controller;

import com.alibaba.fastjson.JSONArray;
import com.focus.springboottest.entity.Candlestick;
import com.focus.springboottest.service.BinanceCandlestickPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api")
public class CandlestickController {

    @Autowired
    private BinanceCandlestickPrinter priceReminderService;

    @GetMapping("/candlesticks")
    public List<Map<String, Object>> getCandlesticks(
            @RequestParam String symbol,
            @RequestParam String interval,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime) {
        try {
            // 调用 Binance API 获取 K 线数据
            List<Candlestick> klines = null;

            if (startTime != null || endTime != null) {
                klines = BinanceCandlestickPrinter.getKlinesByTime(symbol, interval, startTime, endTime);
            } else {
                klines = BinanceCandlestickPrinter.getKlines(symbol, interval, limit != null ? limit : 50);
            }

            // 转换为前端需要的格式
            List<Map<String, Object>> candlesticks = new ArrayList<>();
            for (int i = 0; i < klines.size(); i++) {
                Candlestick candleData = klines.get(i);
                Map<String, Object> candle = new HashMap<>();
                candle.put("time", candleData.getOpenTime());
                candle.put("open", candleData.getOpen());
                candle.put("high", candleData.getHigh());
                candle.put("low", candleData.getLow());
                candle.put("close", candleData.getClose());
                candle.put("volume", candleData.getVolume());
                candle.put("closeTime", candleData.getCloseTime());
                candle.put("quoteAssetVolume", candleData.getQuoteVolume());
                candle.put("numberOfTrades", candleData.getTradeCount());
                candle.put("takerBuyBaseAssetVolume", candleData.getTakerBuyBaseVolume());
                candle.put("takerBuyQuoteAssetVolume", candleData.getTakerBuyQuoteVolume());
                candle.put("ignore", candleData.getIgnore());
                //计算出涨跌幅，用百分比展示
                double open = Double.parseDouble(candleData.getOpen().toString());
                double close = Double.parseDouble(candleData.getClose().toString());
                double change = ((close - open) / open) * 100;
                candle.put("change", String.format("%.2f%%", change));
                //计算出振幅
                double high = Double.parseDouble(candleData.getHigh().toString());
                double low = Double.parseDouble(candleData.getLow().toString());
                double amplitude = ((high - low) / low) * 100;
                candle.put("amplitude", String.format("%.2f%%", amplitude));
                candlesticks.add(candle);
            }
            return candlesticks;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    //获取当前价格
    @GetMapping("/currentPrice")
    public String getCurrentPrice(@RequestParam String symbol) {
        try {
            // 调用 Binance API 获取当前价格
            return BinanceCandlestickPrinter.getCurrentPrice(symbol);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/reminder/{symbol}")
    public ResponseEntity<String> triggerPriceReminder(@PathVariable String symbol,
                                                       @RequestParam String threshold) throws Exception {
        priceReminderService.realTimePriceReminder(symbol, threshold,null);
        return ResponseEntity.accepted().body("价格提醒任务已提交");
    }

}
