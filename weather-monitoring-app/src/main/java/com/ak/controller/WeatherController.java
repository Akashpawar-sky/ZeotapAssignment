package com.ak.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ak.entity.Weather;
import com.ak.service.WeatherService;

@Controller
public class WeatherController {
    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }
    @GetMapping("/fetch")
    public String fetchWeather(Model model) {
        weatherService.fetchWeatherData("Delhi"); // Replace with any city name
        return "redirect:/weather";
    }
    @GetMapping("/weather")
    public String getWeatherData(Model model) {
        List<Weather> weatherList = weatherService.getAllWeatherData();
        model.addAttribute("weatherList", weatherList);
        return "weather"; // Thymeleaf template name
    }

}
