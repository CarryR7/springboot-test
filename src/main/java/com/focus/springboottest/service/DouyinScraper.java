package com.focus.springboottest.service;


import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpResponse;

public class DouyinScraper {

    public static String fetchDouyinPageWithCookies(String url, String cookies) {
        try {
            // 创建HttpClient
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet(url);

            // 设置Cookies
            request.setHeader("Cookie", cookies);

            // 执行请求
            HttpResponse response = httpClient.execute(request);

            // 返回网页内容
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        // 填写要抓取的抖音页面URL和您的Cookies
        String url = "https://www.douyin.com";
        String cookies = "x-web-secsdk-uid=f6817bb6-322f-4d2c-88dc-f09e0179e032";  // 从浏览器中获取并替换为您的Cookies

        // 获取页面内容
        String pageContent = fetchDouyinPageWithCookies(url, cookies);

        // 输出网页内容
        if (pageContent != null) {
            System.out.println(pageContent);
        } else {
            System.out.println("无法获取页面内容");
        }
    }
}



