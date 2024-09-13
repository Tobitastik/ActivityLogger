package org.example.entities;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.ActivityEnums;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "activities")
@Getter
@Setter
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate exerciseDate;
    private ActivityEnums exerciseType;
    private LocalTime timeOfDay;
    private double duration;
    private double distance;
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private CityInfo cityInfo;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "weather_id")
    private WeatherInfo weatherInfo;

}
