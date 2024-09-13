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
    private List<Double> visualCenter;

}