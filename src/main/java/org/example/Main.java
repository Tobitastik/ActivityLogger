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

            // Create CityInfoDTO for Sample 1
            CityInfoDTO cityInfo1 = CityInfoDTO.builder()
                    .name("New York")
                    .visualCenter(List.of(40.7128, -74.0060)) // Latitude and Longitude
                    .build();

            // Create WeatherInfoDTO for Sample 1
            WeatherInfoDTO weatherInfo1 = WeatherInfoDTO.builder()
                    .locationName("New York")
                    .currentData(new WeatherInfoDTO.CurrentData("Clear skies", 25.5, "10 km/h", 65)) // Wind as String
                    .build();

            // Create ActivityDTO for Sample 1
            ActivityDTO activityDTO1 = ActivityDTO.builder()
                    .exerciseDate(LocalDate.of(2023, 9, 14))
                    .exerciseType(ActivityEnums.RUNNING)  // Assuming you have RUNNING in your enum
                    .timeOfDay(LocalTime.of(7, 30))  // 7:30 AM
                    .duration(1.5)  // 1.5 hours
                    .distance(10.0) // 10 km
                    .comment("Morning run in Central Park.")
                    .cityInfo(cityInfo1)
                    .weatherInfo(weatherInfo1)
                    .build();

            // Save Sample 1 in the database
            activityDAO.createOrUpdateActivity(activityDTO1);

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
