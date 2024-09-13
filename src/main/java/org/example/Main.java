package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.example.config.HibernateConfig;
import org.example.dtos.CityInfoDTO;
import org.example.dtos.WeatherInfoDTO;
import org.example.entities.CityInfo;
import org.example.entities.WeatherInfo;
import org.example.entities.Activity;
import org.example.enums.ActivityEnums;
import org.example.services.CityService;
import org.example.services.WeatherService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {


        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("activitylogger");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            // Fetch data using services
            WeatherInfoDTO weatherInfoDTO = WeatherService.fetchWeatherDataByLocationName("Roskilde");
            CityInfoDTO cityInfoDTO = CityService.getCityInfo("Roskilde");

            // Convert DTOs to entities
            WeatherInfo weatherInfo = new WeatherInfo();
            weatherInfo.setLocationName(weatherInfoDTO.getLocationName());
            weatherInfo.setSkyText(weatherInfoDTO.getCurrentData().getSkyText());
            weatherInfo.setTemperature(weatherInfoDTO.getCurrentData().getTemperature());
            weatherInfo.setWindText(weatherInfoDTO.getCurrentData().getWindText());
            weatherInfo.setHumidity(weatherInfoDTO.getCurrentData().getHumidity());

            CityInfo cityInfo = new CityInfo();
            cityInfo.setName(cityInfoDTO.getName());
            cityInfo.setVisualCenter(cityInfoDTO.getVisualCenter());

            // Build Activity entity
            Activity activity = new Activity();
            activity.setExerciseType(ActivityEnums.SWIMMING);  // Save as string
            activity.setCityInfo(cityInfo);
            activity.setDistance(2);
            activity.setExerciseDate(LocalDate.now());
            activity.setDuration(30.0);
            activity.setTimeOfDay(LocalTime.of(05, 45));
            activity.setComment("Morning swim");
            activity.setWeatherInfo(weatherInfo);

            // Persist entities in the correct order
            em.persist(weatherInfo);  // Persist weather info first as it's associated with activity
            em.persist(cityInfo);     // Persist city info
            em.persist(activity);     // Persist the activity

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            em.close();
            emf.close();
        }

    }
}