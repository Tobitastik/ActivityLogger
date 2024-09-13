package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.config.HibernateConfig;
import org.example.dtos.WeatherInfoDTO;
import org.example.entities.WeatherInfo;

public class WeatherInfoDAO {

    private EntityManager entityManager;

    public WeatherInfoDAO() {
        this.entityManager = HibernateConfig.getEntityManagerFactory("activitylogger").createEntityManager();
    }

    public WeatherInfo findByDTO(WeatherInfoDTO weatherInfoDTO) {
        try {
            return entityManager.createQuery("SELECT w FROM WeatherInfo w WHERE w.locationName = :locationName", WeatherInfo.class)
                    .setParameter("locationName", weatherInfoDTO.getLocationName())
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public WeatherInfoDTO save(WeatherInfoDTO weatherInfoDTO) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            // Convert DTO to entity
            WeatherInfo weatherInfo = new WeatherInfo();
            weatherInfo.setLocationName(weatherInfoDTO.getLocationName());
            weatherInfo.setSkyText(weatherInfoDTO.getCurrentData().getSkyText());
            weatherInfo.setTemperature(weatherInfoDTO.getCurrentData().getTemperature());
            weatherInfo.setWindText(weatherInfoDTO.getCurrentData().getWindText());
            weatherInfo.setHumidity(weatherInfoDTO.getCurrentData().getHumidity());

            // Persist the weather info
            entityManager.persist(weatherInfo);
            transaction.commit();

            // Return DTO with ID
            return WeatherInfoDTO.builder()
                    .locationName(weatherInfo.getLocationName())
                    .currentData(new WeatherInfoDTO.CurrentData(
                            weatherInfo.getSkyText(),
                            weatherInfo.getTemperature(),
                            weatherInfo.getWindText(),
                            weatherInfo.getHumidity()))
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
