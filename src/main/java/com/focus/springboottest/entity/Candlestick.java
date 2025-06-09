package com.focus.springboottest.entity;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * K线数据实体（Binance/OKX等交易所通用结构）
 */
@Data
public class Candlestick {

    // 核心OHLCV字段
    private Long openTime;          // K线开始时间（毫秒级时间戳）
    private BigDecimal open;       // 开盘价
    private BigDecimal high;       // 最高价
    private BigDecimal low;        // 最低价
    private BigDecimal close;      // 收盘价
    private BigDecimal volume;     // 成交量（基础货币，如BTC）

    // 扩展字段
    private Long closeTime;        // K线结束时间
    private BigDecimal quoteVolume; // 成交额（报价货币，如USDT）
    private Integer tradeCount;    // 成交笔数
    private BigDecimal takerBuyBaseVolume;  // 主动买入的基币成交量
    private BigDecimal takerBuyQuoteVolume; // 主动买入的报价币成交额

    // 忽略字段（根据需求可选）
    private String ignore;

    // 时间戳转Date（便捷方法）
    public Date getOpenTimeDate() {
        return new Date(openTime);
    }

    public Date getCloseTimeDate() {
        return new Date(closeTime);
    }

    // 计算价格变化百分比
    public BigDecimal getPriceChangePercent() {
        return close.subtract(open)
                .divide(open, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}