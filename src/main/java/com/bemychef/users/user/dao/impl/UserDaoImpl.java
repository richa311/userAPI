package com.bemychef.users.user.dao.impl;

import com.bemychef.users.user.dao.UserDao;
import com.bemychef.users.user.model.User;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Named("userDaoBean")
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public long checkIfEmailAlreadyExists(String emailId) {
        Query query = em.createQuery("SELECT u FROM User u WHERE u.emailId = :emailId");
        query.setParameter("emailId", emailId);
        return query.getResultStream().count();
    }

    @Override
    public void updateUserDetails(User user) {
        Query query = em.createQuery("UPDATE USER SET firstName =:firstName, lastName =:lastName, middleName =:middleName" +
                "contactNumber =:contactNumber, emailId =:emailId, status =:status, createdBy =:createdBy, createdOn =:createdOn," +
                "modifiedOn =:modifiedOn, modifiedBy =:modifiedBy");
        query.setParameter("firstName", user.getFirstName());
        query.setParameter("lastName", user.getLastName());
        query.setParameter("middleName", user.getMiddleName());
        query.setParameter("contactNumber", user.getContactNumber());
        query.setParameter("emailId", user.getEmailId());
        query.setParameter("status", user.getStatus());
        query.setParameter("createdBy", user.getCreatedBy());
        query.setParameter("createdOn", user.getCreatedOn());
        query.setParameter("modifiedOn", user.getModifiedOn());
        query.setParameter("modifiedBy", user.getModifiedBy());

        query.executeUpdate();
    }

    @Override
    public void updateRegistrationDetails(User user) {

    }
}
