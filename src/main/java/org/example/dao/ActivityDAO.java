package org.example.dao;

import jakarta.persistence.EntityManager;
import org.example.dtos.ActivityDTO;
import org.example.dtos.CityInfoDTO;
import org.example.dtos.WeatherInfoDTO;
import org.example.entities.Activity;
import org.example.entities.CityInfo;
import org.example.entities.WeatherInfo;

public class ActivityDAO {

    private final EntityManager entityManager;

    public ActivityDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Method to create or update an activity with city info and weather info
    public ActivityDTO createOrUpdateActivity(ActivityDTO activityDTO) {
        // Handle CityInfo entity
        CityInfo cityInfo = getOrCreateCityInfo(activityDTO.getCityInfo());

        // Handle WeatherInfo entity
        WeatherInfo weatherInfo = getOrCreateWeatherInfo(activityDTO.getWeatherInfo());

        // Create and set Activity entity
        Activity activity = new Activity();
        activity.setExerciseDate(activityDTO.getExerciseDate());
        activity.setExerciseType(activityDTO.getExerciseType());
        activity.setTimeOfDay(activityDTO.getTimeOfDay());
        activity.setDuration(activityDTO.getDuration());
        activity.setDistance(activityDTO.getDistance());
        activity.setComment(activityDTO.getComment());
        activity.setCityInfo(cityInfo);  // Set the associated city info
        activity.setWeatherInfo(weatherInfo);  // Set the associated weather info

        entityManager.persist(activity);

        // Return the DTO including the database-generated ID for the activity
        activityDTO.setId(activity.getId());
        return activityDTO;
    }

    // Method to get or create CityInfo entity based on the DTO
    private CityInfo getOrCreateCityInfo(CityInfoDTO cityInfoDTO) {
        CityInfo cityInfo = entityManager
                .createQuery("SELECT c FROM CityInfo c WHERE c.name = :name", CityInfo.class)
                .setParameter("name", cityInfoDTO.getName())
                .getResultStream().findFirst().orElse(null);

        if (cityInfo == null) {
            cityInfo = new CityInfo();
            cityInfo.setName(cityInfoDTO.getName());
            cityInfo.setVisualCenter(cityInfoDTO.getVisualCenter());  // Use visualCenter directly
            entityManager.persist(cityInfo);
        }
        return cityInfo;
    }

    // Method to get or create WeatherInfo entity based on the DTO
    private WeatherInfo getOrCreateWeatherInfo(WeatherInfoDTO weatherInfoDTO) {
        WeatherInfo weatherInfo = entityManager
                .createQuery("SELECT w FROM WeatherInfo w WHERE w.locationName = :locationName", WeatherInfo.class)
                .setParameter("locationName", weatherInfoDTO.getLocationName())
                .getResultStream().findFirst().orElse(null);

        if (weatherInfo == null) {
            weatherInfo = new WeatherInfo();
            weatherInfo.setLocationName(weatherInfoDTO.getLocationName());
            weatherInfo.setTemperature(weatherInfoDTO.getCurrentData().getTemperature());
            weatherInfo.setWindText(weatherInfoDTO.getCurrentData().getWindText());  // Wind speed is a string (windText)
            weatherInfo.setHumidity(weatherInfoDTO.getCurrentData().getHumidity());
            weatherInfo.setSkyText(weatherInfoDTO.getCurrentData().getSkyText());
            entityManager.persist(weatherInfo);
        }
        return weatherInfo;
    }

    // Method to retrieve an activity by its ID
    public ActivityDTO getActivityById(Long id) {
        Activity activity = entityManager.find(Activity.class, id);
        if (activity == null) return null;

        // Convert the Activity entity to ActivityDTO
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setId(activity.getId());
        activityDTO.setExerciseDate(activity.getExerciseDate());
        activityDTO.setExerciseType(activity.getExerciseType());
        activityDTO.setTimeOfDay(activity.getTimeOfDay());
        activityDTO.setDuration(activity.getDuration());
        activityDTO.setDistance(activity.getDistance());
        activityDTO.setComment(activity.getComment());

        // Set CityInfoDTO and WeatherInfoDTO from the associated entities
        CityInfoDTO cityInfoDTO = new CityInfoDTO();
        cityInfoDTO.setName(activity.getCityInfo().getName());
        cityInfoDTO.setVisualCenter(activity.getCityInfo().getVisualCenter());  // Set visualCenter directly
        activityDTO.setCityInfo(cityInfoDTO);

        WeatherInfoDTO weatherInfoDTO = new WeatherInfoDTO();
        weatherInfoDTO.setLocationName(activity.getWeatherInfo().getLocationName());
        weatherInfoDTO.getCurrentData().setTemperature(activity.getWeatherInfo().getTemperature());
        weatherInfoDTO.getCurrentData().setWindText(activity.getWeatherInfo().getWindText());
        weatherInfoDTO.getCurrentData().setHumidity(activity.getWeatherInfo().getHumidity());
        weatherInfoDTO.getCurrentData().setSkyText(activity.getWeatherInfo().getSkyText());
        activityDTO.setWeatherInfo(weatherInfoDTO);

        return activityDTO;
    }
}
