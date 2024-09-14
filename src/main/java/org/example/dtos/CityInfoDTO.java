package org.example.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityInfoDTO {
    @JsonSetter("primærtnavn")
    private String name;
    @JsonSetter("visueltcenter")
    private List<Double> visualCenter;  // We’ll assume the first two elements are latitude and longitude

    public Double getLatitude() {
        return visualCenter != null && visualCenter.size() > 0 ? visualCenter.get(0) : null;
    }

    public Double getLongitude() {
        return visualCenter != null && visualCenter.size() > 1 ? visualCenter.get(1) : null;
    }
}
