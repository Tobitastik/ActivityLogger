package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.example.config.HibernateConfig;
import org.example.dao.ActivityDAO;
import org.example.dtos.ActivityDTO;
import org.example.dtos.CityInfoDTO;
import org.example.dtos.WeatherInfoDTO;
import org.example.enums.ActivityEnums;
import org.example.services.CityService;
import org.example.services.WeatherService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("activitylogger");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        ActivityDAO activityDAO = new ActivityDAO(em);

        try {
            transaction.begin();

            String city = "Odense";

            // Use CityService to fetch city info
            CityInfoDTO cityInfo = CityService.getCityInfo(city); // Example Danish city

            // Use WeatherService to fetch weather info
            WeatherInfoDTO weatherInfo = WeatherService.fetchWeatherDataByLocationName(city); // Example Danish city

            if (cityInfo != null && weatherInfo != null) {
                // Create ActivityDTO using the fetched data
                ActivityDTO activityDTO = ActivityDTO.builder()
                        .exerciseDate(LocalDate.of(2024, 9, 14))
                        .exerciseType(ActivityEnums.CYCLING)  // Assuming you have RUNNING in your enum
                        .timeOfDay(LocalTime.of(15, 00))  // 7:30 AM
                        .duration(8)  // 1.5 hours
                        .distance(210.0) // 10 km
                        .comment("Tour De Denmark")
                        .cityInfo(cityInfo)
                        .weatherInfo(weatherInfo)
                        .build();

                // Save the activity in the database
                activityDAO.createOrUpdateActivity(activityDTO);

                transaction.commit();
            } else {
                System.out.println("Failed to fetch city or weather information.");
            }
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
