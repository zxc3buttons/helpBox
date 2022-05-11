package com.example.helpbox.controller;

import com.example.helpbox.model.User;
import com.example.helpbox.repository.UserRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/auth")
public class HolidaysController {

    public int DATE = LocalDateTime.now().getDayOfMonth();
    public int MONTH = LocalDateTime.now().getMonthValue();
    public String URL = "https://www.calend.ru/holidays/" + MONTH + "-" + DATE;

    private final UserRepository userRepository;
    @Autowired
    public HolidaysController (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, String> result = new HashMap<>();

    public User getCurrentUser () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = ((UserDetails)principal).getUsername();
        User user = userRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("User doesn't exists"));
        return user;
    }


    public void parseHolidays() throws IOException {
        Document document = Jsoup.connect(URL)
                .userAgent("Microsoft Edge")
                .timeout(5000)
                .referrer("https://google.com")
                .get();
        Elements holidays = document.select("body > div.wrapper > div.block.main" +
                " > div.block.content > div.block.datesList > div.block.holidays > ul.itemsNet > li.three-three" +
                " > div.caption > span.title");
        if (holidays.isEmpty()) {
            throw new IOException();
        }
        else {
            Elements URLs = document.select("body > div.wrapper > div.block.main" +
                    " > div.block.content > div.block.datesList > div.block.holidays > ul.itemsNet > li.three-three" +
                    " > div.caption > p.descr.descrFixed > a");
            for (int i = 0; i < URLs.size(); i++) {
                Document doc = Jsoup.connect(URLs.eachAttr("href").get(i))
                        .userAgent("Microsoft Edge")
                        .timeout(5000)
                        .referrer("https://google.com")
                        .get();
                String description = "";
                Elements about = doc.select("body > div.wrapper > div.block.main" +
                        " > div.block.content > div.maintext > p");
                if (about.hasText()) {
                    for (Element e : about) {
                        description += e.text();
                    }
                } else {
                    description = "Невозможно найти описание для данного праздника";
                }
                result.put(holidays.eachText().get(i), description);
            }
        }
    }
    @GetMapping("/holidays")
    public String getHolidays(Model model) throws IOException {
        if (!result.isEmpty()) {
            result.clear();
        }
        try {
            parseHolidays();
        } catch (IOException e) {
            model.addAttribute("exception", "Проверьте правильность введенных вами данных");
        }
        DATE = LocalDateTime.now().getDayOfMonth();
        MONTH = LocalDateTime.now().getMonthValue();
        URL = "https://www.calend.ru/holidays/" + MONTH + "-" + DATE;
        model.addAttribute("holidays", result);
        model.addAttribute("user", getCurrentUser());
        return "holidays";
    }

    @PostMapping("/holidays")
    public String getDayAndMonth(@RequestParam String date, @RequestParam String month, Model model) {
        this.DATE = Integer.parseInt(date);
        this.MONTH = Integer.parseInt(month);
        this.URL = "https://www.calend.ru/holidays/" + MONTH + "-" + DATE;
        return "redirect:/auth/holidays";
    }
}
