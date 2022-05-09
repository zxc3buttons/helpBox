package com.example.helpbox.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


@Controller
@RequestMapping("/auth")
public class HolidaysController {

    public final String URL = "https://www.calend.ru/holidays/1-8";

    public Map<String, String> result = new Hashtable<>();

    public String getCurrentUrl() {
        LocalDateTime current = LocalDateTime.now();
        int month = current.getMonthValue();
        int date = current.getDayOfMonth();
        return "https://www.calend.ru/holidays/" + month + "-" + date;
    }


    @Scheduled(fixedDelay = 10000)
    public void parseHolidays() {
        try {
            Document document = Jsoup.connect(URL)
                    .userAgent("Microsoft Edge")
                    .timeout(5000)
                    .referrer("https://google.com")
                    .get();
            Elements holidays = document.select("body > div.wrapper > div.block.main" +
                    " > div.block.content > div.block.datesList > div.block.holidays > ul.itemsNet > li.three-three" +
                    " > div.caption > span.title");
            Elements URLs = document.select("body > div.wrapper > div.block.main" +
                    " > div.block.content > div.block.datesList > div.block.holidays > ul.itemsNet > li.three-three" +
                    " > div.caption > p.descr.descrFixed > a");
            for (int i = 0; i < URLs.size(); i++) {
                Document doc = Jsoup.connect(URLs.eachAttr("href").get(i))
                        .userAgent("Microsoft Edge")
                        .timeout(5000)
                        .referrer("https://google.com")
                        .get();
                Elements about = doc.select("body > div.wrapper > div.block.main" +
                        " > div.block.content > div.maintext > p");
                String text = "";
                for (int j = 0; j < 2; j++) {
                    text += about.eachText().get(j);
                }
                result.put(holidays.eachText().get(i), text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/holidays")
    public String getHolidays(Model model) {
        model.addAttribute("holidays", result);
        return "holidays";
    }

    @PostMapping("/holidays")
    public String getDayAndMonth() {
        return "something";
    }
}
