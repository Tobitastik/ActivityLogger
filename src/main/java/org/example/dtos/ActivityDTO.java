package org.example.dtos;

import org.example.enums.ActivityEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@ToString
@AllArgsConstructor
@Builder
public class ActivityDTO {
    private LocalDate exerciseDate;
    private ActivityEnums exerciseType;
    private LocalTime timeOfDay;
    private double duration;  // In hours, for example
    private double distance;  // In kilometers or miles
    private String comment;
    private CityInfoDTO cityInfo;
    private WeatherInfoDTO weatherInfo;
}