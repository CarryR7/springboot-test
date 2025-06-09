package com.focus.springboottest;

import com.alibaba.fastjson.JSONArray;
import com.focus.springboottest.service.BinanceCandlestickPrinter;
import com.focus.springboottest.util.EmailUtil;
import okhttp3.Request;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BinanceCandlestickPrinterIntegrationTest {

    @Autowired
    private EmailUtil emailUtil;

    @Test
    @DisplayName("测试有效Symbol获取当前价格")
    void testGetCurrentPrice_ValidSymbol() {
        try {
            String result = BinanceCandlestickPrinter.getCurrentPrice("BTCUSDT");
            assertNotNull(result, "返回结果不应为空");

            // 示例输出：{"symbol":"BTCUSDT","price":"63000.12"}
            System.out.println("BTCUSDT 当前价格: " + result);
        } catch (Exception e) {
            fail("有效Symbol不应抛出异常: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("测试无效Symbol获取当前价格")
    void testGetCurrentPrice_InvalidSymbol() {
        Exception exception = assertThrows(Exception.class, () -> {
            BinanceCandlestickPrinter.getCurrentPrice("INVALIDSYMBOL123");
        });

        assertTrue(exception.getMessage().contains("400") ||
                        exception.getMessage().toLowerCase().contains("json"),
                "预期HTTP错误或JSON解析错误");
    }


    @Test
    @DisplayName("测试邮件发送情况")
    void sendTestEmail() {
        String to = "1106288595@qq.com";
        String subject = "测试邮件 - Spring Boot";
        String text = "这是一个简单的文本邮件。";

        emailUtil.sendSimpleEmail(to, subject, text);

    }

}

