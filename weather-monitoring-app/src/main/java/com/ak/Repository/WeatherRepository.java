package com.ak.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ak.entity.Weather;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    List<Weather> findByCity(String city);
}

