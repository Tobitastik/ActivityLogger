package org.example.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WeatherInfoDTO {
    private String locationName;
    private String typeOfWeather;
    private int temperature;
    private int windSpeed;
    private int humidity;
}
