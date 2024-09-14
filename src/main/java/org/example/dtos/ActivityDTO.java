package org.example.dtos;

import lombok.*;
import org.example.enums.ActivityEnums;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityDTO {
    private Long id;  // Add this field for setting/getting activity ID
    private LocalDate exerciseDate;
    private ActivityEnums exerciseType;
    private LocalTime timeOfDay;
    private double duration;  // In hours, for example
    private double distance;  // In kilometers or miles
    private String comment;
    private CityInfoDTO cityInfo;
    private WeatherInfoDTO weatherInfo;
}
