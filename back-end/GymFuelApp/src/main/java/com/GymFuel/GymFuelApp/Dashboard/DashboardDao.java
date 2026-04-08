package com.GymFuel.GymFuelApp.Dashboard;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DashboardDao {
    @PersistenceContext
    private EntityManager entityManager;

    public Optional<DashboardEntity> findByEmail(String email) {
        String jpql = "SELECT s FROM DashboardEntity s JOIN s.user u WHERE u.emailAddress = :email";

        // Using getResultList() is much safer than getSingleResult()
        java.util.List<DashboardEntity> results = entityManager.createQuery(jpql, DashboardEntity.class)
                .setParameter("email", email)
                .getResultList();

        if (results.isEmpty()) {
            return Optional.empty();
        }

        // If there are multiple, just return the first one found
        return Optional.of(results.get(0));
    }

    @Transactional
    public void save(DashboardEntity state) {
        if (state.getId() == null) {
            entityManager.persist(state);
        } else {
            entityManager.merge(state);
        }
    }
}
