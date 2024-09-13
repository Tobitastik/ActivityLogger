package org.example.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeatherInfoDTO {
    @JsonSetter("LocationName")
    private String locationName;
    @JsonSetter("CurrentData")
    private CurrentData currentData;


    @Data
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CurrentData {
        private String skyText;
        private double temperature;
        private String windText;
        private int humidity;
    }
}
