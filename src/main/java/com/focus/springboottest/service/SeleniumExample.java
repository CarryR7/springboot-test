package com.focus.springboottest.service;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class SeleniumExample {
    public static void main(String[] args) {
        // 设置 ChromeDriver 的路径
        System.setProperty("webdriver.chrome.driver", "D:\\soft\\chrome-win64\\chrome.exe");

        // 设置 ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // 在后台运行，不显示浏览器界面
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");

        // 创建 WebDriver 实例
        WebDriver driver = new ChromeDriver(options);

        try {
            // 访问目标网页
            driver.get("https://gaokao.chsi.com.cn/gkzt/jcxkzs");

            // 等待页面加载完成，直到目标元素可见
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("part-gxmd-box")));

            // 获取所有 a 标签
            List<WebElement> links = driver.findElements(By.cssSelector("div.part-gxmd-box a"));

            // 遍历所有 a 标签并输出 href 属性
            for (WebElement link : links) {
                System.out.println(link.getAttribute("href"));
            }
        } finally {
            // 关闭浏览器
            driver.quit();
        }
    }
}

