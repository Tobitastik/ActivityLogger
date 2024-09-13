package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.config.HibernateConfig;
import org.example.dtos.CityInfoDTO;
import org.example.entities.CityInfo;

public class CityInfoDAO {

    private EntityManager entityManager;

    public CityInfoDAO() {
        this.entityManager = HibernateConfig.getEntityManagerFactory("activitylogger").createEntityManager();
    }

    public CityInfo findByDTO(CityInfoDTO cityInfoDTO) {
        try {
            return entityManager.createQuery("SELECT c FROM CityInfo c WHERE c.name = :name", CityInfo.class)
                    .setParameter("name", cityInfoDTO.getName())
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public CityInfoDTO save(CityInfoDTO cityInfoDTO) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            // Convert DTO to entity
            CityInfo cityInfo = new CityInfo();
            cityInfo.setName(cityInfoDTO.getName());
            cityInfo.setVisualCenter(cityInfoDTO.getVisualCenter());

            // Persist the city info
            entityManager.persist(cityInfo);
            transaction.commit();

            // Return DTO with ID
            return CityInfoDTO.builder()
                    .name(cityInfo.getName())
                    .visualCenter(cityInfo.getVisualCenter())
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
