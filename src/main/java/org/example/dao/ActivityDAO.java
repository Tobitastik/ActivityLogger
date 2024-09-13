package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.config.HibernateConfig;
import org.example.dtos.ActivityDTO;
import org.example.dtos.CityInfoDTO;
import org.example.dtos.WeatherInfoDTO;
import org.example.entities.Activity;
import org.example.entities.CityInfo;
import org.example.entities.WeatherInfo;

public class ActivityDAO {

    private EntityManager entityManager;

    public ActivityDAO() {
        this.entityManager = HibernateConfig.getEntityManagerFactory("activitylogger").createEntityManager();
    }

    public ActivityDTO save(ActivityDTO activityDTO) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            // Convert DTO to entity
            Activity activity = new Activity();
            activity.setExerciseDate(activityDTO.getExerciseDate());
            activity.setExerciseType(activityDTO.getExerciseType());
            activity.setTimeOfDay(activityDTO.getTimeOfDay());
            activity.setDuration(activityDTO.getDuration());
            activity.setDistance(activityDTO.getDistance());
            activity.setComment(activityDTO.getComment());

            // Use CityInfoDAO and WeatherInfoDAO to handle city and weather information
            CityInfoDAO cityInfoDAO = new CityInfoDAO();
            WeatherInfoDAO weatherInfoDAO = new WeatherInfoDAO();

            CityInfoDTO cityInfoDTO = activityDTO.getCityInfo();
            WeatherInfoDTO weatherInfoDTO = activityDTO.getWeatherInfo();

            CityInfo cityInfo = cityInfoDAO.findByDTO(cityInfoDTO);
            if (cityInfo == null) {
                // If the city info does not exist, save it
                cityInfoDTO = cityInfoDAO.save(cityInfoDTO);
                cityInfo = new CityInfo();
                cityInfo.setName(cityInfoDTO.getName());
                cityInfo.setVisualCenter(cityInfoDTO.getVisualCenter());
            }

            WeatherInfo weatherInfo = weatherInfoDAO.findByDTO(weatherInfoDTO);
            if (weatherInfo == null) {
                // If the weather info does not exist, save it
                weatherInfoDTO = weatherInfoDAO.save(weatherInfoDTO);
                weatherInfo = new WeatherInfo();
                weatherInfo.setLocationName(weatherInfoDTO.getLocationName());
                weatherInfo.setSkyText(weatherInfoDTO.getCurrentData().getSkyText());
                weatherInfo.setTemperature(weatherInfoDTO.getCurrentData().getTemperature());
                weatherInfo.setWindText(weatherInfoDTO.getCurrentData().getWindText());
                weatherInfo.setHumidity(weatherInfoDTO.getCurrentData().getHumidity());
            }

            activity.setCityInfo(cityInfo);
            activity.setWeatherInfo(weatherInfo);

            // Persist the activity
            entityManager.persist(activity);
            transaction.commit();

            // Return DTO with ID
            return ActivityDTO.builder()
                    .exerciseDate(activity.getExerciseDate())
                    .exerciseType(activity.getExerciseType())
                    .timeOfDay(activity.getTimeOfDay())
                    .duration(activity.getDuration())
                    .distance(activity.getDistance())
                    .comment(activity.getComment())
                    .cityInfo(CityInfoDTO.builder()
                            .name(cityInfo.getName())
                            .visualCenter(cityInfo.getVisualCenter())
                            .build())
                    .weatherInfo(WeatherInfoDTO.builder()
                            .locationName(weatherInfo.getLocationName())
                            .currentData(new WeatherInfoDTO.CurrentData(
                                    weatherInfo.getSkyText(),
                                    weatherInfo.getTemperature(),
                                    weatherInfo.getWindText(),
                                    weatherInfo.getHumidity()))
                            .build())
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            if (transaction.isActive()) {
                transaction.rollback();
            }
            return null;
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
}
