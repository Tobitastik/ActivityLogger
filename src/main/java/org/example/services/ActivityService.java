package org.example.services;

import org.example.dao.ActivityDAO;
import org.example.dao.CityInfoDAO;
import org.example.dao.WeatherInfoDAO;
import org.example.dtos.ActivityDTO;
import org.example.dtos.CityInfoDTO;
import org.example.dtos.WeatherInfoDTO;

public class ActivityService {

    private final ActivityDAO activityDAO;
    private final CityInfoDAO cityInfoDAO;
    private final WeatherInfoDAO weatherInfoDAO;

    public ActivityService() {
        this.activityDAO = new ActivityDAO();
        this.cityInfoDAO = new CityInfoDAO();
        this.weatherInfoDAO = new WeatherInfoDAO();
    }

    public ActivityDTO saveActivity(ActivityDTO activityDTO) {
        // Save CityInfo and WeatherInfo if they don't exist and retrieve the updated DTOs
        CityInfoDTO cityInfoDTO = activityDTO.getCityInfo();
        if (cityInfoDTO != null) {
            CityInfoDTO savedCityInfoDTO = cityInfoDAO.save(cityInfoDTO);
            activityDTO.setCityInfo(savedCityInfoDTO);
        }

        WeatherInfoDTO weatherInfoDTO = activityDTO.getWeatherInfo();
        if (weatherInfoDTO != null) {
            WeatherInfoDTO savedWeatherInfoDTO = weatherInfoDAO.save(weatherInfoDTO);
            activityDTO.setWeatherInfo(savedWeatherInfoDTO);
        }

        // Save the activity
        return activityDAO.save(activityDTO);
    }
}
