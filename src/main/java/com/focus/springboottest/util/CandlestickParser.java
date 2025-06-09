package com.focus.springboottest.util;

import com.focus.springboottest.entity.Candlestick;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class CandlestickParser {

    /**
     * 将交易所API返回的原始数组转换为Candlestick对象
     * @param rawData 例如: [1499040000000, "0.01634790", "0.80000000", ...]
     */
    public static Candlestick parse(List<?> rawData) {
        if (rawData == null || rawData.size() < 11) {
            throw new IllegalArgumentException("Invalid K线数据格式");
        }

        Candlestick candle = new Candlestick();
        candle.setOpenTime(parseLong(rawData.get(0)));
        candle.setOpen(parseDecimal(rawData.get(1)));
        candle.setHigh(parseDecimal(rawData.get(2)));
        candle.setLow(parseDecimal(rawData.get(3)));
        candle.setClose(parseDecimal(rawData.get(4)));
        candle.setVolume(parseDecimal(rawData.get(5)));
        candle.setCloseTime(parseLong(rawData.get(6)));
        candle.setQuoteVolume(parseDecimal(rawData.get(7)));
        candle.setTradeCount(parseInt(rawData.get(8)));
        candle.setTakerBuyBaseVolume(parseDecimal(rawData.get(9)));
        candle.setTakerBuyQuoteVolume(parseDecimal(rawData.get(10)));

        // 忽略字段（索引11）
        if (rawData.size() > 11) {
            candle.setIgnore(rawData.get(11).toString());
        }

        return candle;
    }

    // 类型安全转换方法
    private static Long parseLong(Object value) {
        return value instanceof Number ? ((Number) value).longValue() : Long.parseLong(value.toString());
    }

    private static Integer parseInt(Object value) {
        return value instanceof Number ? ((Number) value).intValue() : Integer.parseInt(value.toString());
    }

    private static BigDecimal parseDecimal(Object value) {
        return new BigDecimal(value.toString());
    }
}