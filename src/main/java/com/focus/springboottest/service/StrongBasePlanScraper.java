package com.focus.springboottest.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;

public class StrongBasePlanScraper {
    public static void main(String[] args) throws IOException {
        String url = "https://gaokao.chsi.com.cn/gkzt/jcxkzs";

        // 伪装浏览器访问
        Document doc = Jsoup.connect(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                .header("Referer", "https://gaokao.chsi.com.cn/")
                .header("Cookie", "aliyungf_tc=d83bbb2b7bc95df6436b67c369916f9438d238d3de3c9d9a4b6846894e9d1d14; goaYXsyEWlxdO=60NZoYy8kor.P5AnHUzU7UrLZ6pDQ1JMPmaXs3l0WZTSTPSdYsahLIX254Ga5w1kiJYhk6VoGGUuurzaHKERh2DA; CHSICC01=!j8pAHcU6WWktMxQnVPBkiJOoJxwY2uC8cEUTw8hA6P0y5fEU78qj4cu43yPIhurlrpLKVlQmtrgS0w==; 35067579362dd9cd78=c58ea6476bd9c117ebaa07bb7def3eea; XSRF-CCKTOKEN=90aced11d66608e309f3bceb0c475c0e; CHSICC_CLIENTFLAGGAOKAO=5afa823e424819a8d1cf4e6169f007fb; Hm_lvt_19141110b831c2c573190bb7a3b0ef3f=1745203430; HMACCOUNT=D8D2DF8B00F52463; _gid=GA1.3.479342945.1745203431; CHSICC_CLIENTFLAGCHSI=905233b5a742f1e8f5681ab828e68c6f; _ga_VEMJM9VZ7D=GS1.1.1745203491.1.1.1745205986.0.0.0; acw_tc=ac11000117452174415921126ee694394417158d9fede3e142864c757d48d9; JSESSIONID=C268919A47426D35483376FBB59F3DE9; _gat_gtag_UA_100524_6=1; _ga_5FHT145N0D=GS1.1.1745217441.3.1.1745217455.0.0.0; _ga=GA1.1.1130954470.1745203431; Hm_lpvt_19141110b831c2c573190bb7a3b0ef3f=1745217456; goaYXsyEWlxdP=0qzdqHk_cn1Da6Dl3tcphsom_YPg2mwXfQ4InTxAzSHRAnBCJJ143m7szFbshdIaH2tVR7KQF1Dn1LGcE2drAldgQUQE8DC020C06LJ3ChuR.YCOtONYFAvepTtR2xx3BZmbA24YLaKZ8Q9EJ.4gJL8fnw0pKN8ItWPkMh7epw.DdU3I5lq082p4wz4n74gtyRdkaKye3yOhYR0VhAj6amnxEo5t_uccCoLVT4OcifMXSJo5RbOxHyu7B7l.vXJEGSriyHjXSj.u5kbvP99Q._wkqFIK.T3NH8GLJ9LOIuGr3EWJV.gE_HGdXgX4vv6BYsCwkK0hlBc3WDPFUeIaMMugO1cJee3I9d1SUCR.itELDsY1GJF.0_PLzPWVeNIkVDomdqtr_pxPkg93vvuWECHVVqQWLrR7XTZ9Etjz3buIVFIhW6kuY2Qn3iCnffHav0EXAxgVuPj_ER6UGij3aCq")
                .timeout(10000)
                .get();

// 打印整个网页内容
        System.out.println(doc.html());



        // 选择 div.part-gxmd-box 下的所有 a 标签
        Elements links = doc.select("div.part-gxmd-box a");

// 遍历所有选中的 a 标签并提取 href 属性
        for (Element link : links) {
            String href = link.attr("href");
            System.out.println(href); // 输出 href
        }



//
//        Elements items = doc.select("div.part-gxmd-box a.gxmd-item");
//
//
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("招生简章");
//
//        // 标题行
//        Row header = sheet.createRow(0);
//        header.createCell(0).setCellValue("学校名称");
//        header.createCell(1).setCellValue("招生简章链接");
//
//        int rowNum = 1;
//
//        for (Element item : items) {
//            Element tag = item.selectFirst("div.gxmd-item-tag");
//            if (tag != null && !tag.hasClass("disabled")) {
//                String schoolName = item.selectFirst("div.gxmd-item-text").text();
//                String link = item.absUrl("href");
//
//                Row row = sheet.createRow(rowNum++);
//                row.createCell(0).setCellValue(schoolName);
//                row.createCell(1).setCellValue(link);
//            }
//        }
//
//        try (FileOutputStream fileOut = new FileOutputStream("招生简章.xlsx")) {
//            workbook.write(fileOut);
//        }
//        workbook.close();
//
//        System.out.println("Excel 文件已生成: 招生简章.xlsx");
    }
}
