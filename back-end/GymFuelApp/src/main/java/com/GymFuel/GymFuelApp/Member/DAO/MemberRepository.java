package com.GymFuel.GymFuelApp.Member.DAO;

import com.GymFuel.GymFuelApp.Member.DTO.LoginMemberDTO;
import com.GymFuel.GymFuelApp.Member.Entity.RegisterMemberEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void registerMeber(RegisterMemberEntity registerMemberEntity) {
        entityManager.persist(registerMemberEntity);
    }

    public Boolean findExistingMemer(String username){
        Long count= (Long) entityManager.createQuery("Select COUNT(s) from RegisterMemberEntity s where s.username=:username")
                .setParameter("username",username)
                .getSingleResult();
        return count>0;
    }

    public boolean emailExists(String emailAddress){
        Long count= (Long) entityManager.createQuery("Select COUNT(s) from RegisterMemberEntity s where s.emailAddress=:emailAddress")
                .setParameter("emailAddress",emailAddress)
                .getSingleResult();
        return count>0;
    }

    public RegisterMemberEntity findMemberByUsername(String username) {
        try {
            RegisterMemberEntity member = entityManager.createQuery("Select S from RegisterMemberEntity S where S.username=:username", RegisterMemberEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return member;
        }catch(NoResultException e){
            return null;
        }
    }
    public RegisterMemberEntity findMemberByEmail(String emailAddress) {
        try {
            return entityManager.createQuery(
                            "SELECT s FROM RegisterMemberEntity s WHERE s.emailAddress = :email",
                            RegisterMemberEntity.class)
                    .setParameter("email", emailAddress)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Return null if the email isn't in our database yet
        }
    }
}
