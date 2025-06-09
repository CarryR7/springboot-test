package com.focus.springboottest.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class PriceInfo {
    private String symbol;
    private String price;
    private Date time;

    // Getters and Setters
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    // toString method
//    @Override
//    public String toString() {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
//        return "PriceInfo{" +
//                "symbol='" + symbol + '\'' +
//                ", price='" + price + '\'' +
//                ", time=" + time.format(formatter) +
//                '}';
//    }

    public PriceInfo(String symbol, String price, Date time) {
        this.symbol = symbol;
        this.price = price;
        this.time = time;
    }
}