package com.ak.service;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ak.Repository.*;
import com.ak.entity.Weather;
@Service
public class WeatherService {
    private final WeatherRepository weatherRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final List<String> cities = List.of("Delhi", "Mumbai", "Chennai", "Bangalore", "Kolkata", "Hyderabad");

    @Value("${openweathermap.api.key}")
    private String apiKey;

    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    @Scheduled(fixedRate = 300000) // Fetch data every 5 minutes
    public void fetchWeatherDataForCities() {
        for (String city : cities) {
            Weather weather = fetchWeatherData(city);
            if (weather != null) {
                System.out.println("Successfully fetched and saved data for: " + city);
            } else {
                System.out.println("Failed to fetch data for: " + city);
            }
        }
    }

    public Weather fetchWeatherData(String city) {
        try {
            String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", city, apiKey);
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                // Parse response to extract weather data
                Map main = (Map) response.getBody().get("main");
                List<Map<String, Object>> weatherList = (List<Map<String, Object>>) response.getBody().get("weather");
                String mainCondition = (String) weatherList.get(0).get("main");

                // Create a Weather object and set its attributes
                Weather weather = new Weather();
                weather.setCity(city);
                weather.setMainCondition(mainCondition);
                weather.setTemperature((Double) main.get("temp") - 273.15); // Kelvin to Celsius
                weather.setFeelsLike((Double) main.get("feels_like") - 273.15);
                weather.setTimestamp(LocalDateTime.now());

                // Save the weather data for the city in the database
                weatherRepository.save(weather);
                return weather;
            } else {
                System.out.println("API response error for city: " + city);
                return null;
            }
        } catch (Exception e) {
            System.out.println("Exception while fetching data for city: " + city + " - " + e.getMessage());
            return null;
        }
    }





    public List<Weather> getAllWeatherData() {
        return weatherRepository.findAll(); // Retrieve all weather data from DB
    }
}
