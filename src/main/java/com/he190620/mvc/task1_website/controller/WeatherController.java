package com.he190620.mvc.task1_website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WeatherController {

    @GetMapping("/weather")
    public String weather() {
        return "weather";
    }
}
