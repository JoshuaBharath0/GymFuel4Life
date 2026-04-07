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
        try {
            // We look through the linked RegisterMemberEntity's emailAddress
            String jpql = "SELECT s FROM DashboardEntity s JOIN s.user u WHERE u.emailAddress = :email";
            DashboardEntity state = entityManager.createQuery(jpql, DashboardEntity.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(state);
        } catch (NoResultException e) {
            return Optional.empty();
        }
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
