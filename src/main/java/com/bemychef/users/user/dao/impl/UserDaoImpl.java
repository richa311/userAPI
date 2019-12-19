package com.bemychef.users.user.dao.impl;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.bemychef.users.security.PasswordEncryption;
import com.bemychef.users.user.dao.UserDao;
import com.bemychef.users.user.model.Status;
import com.bemychef.users.user.model.User;

@Named("userDaoBean")
public class UserDaoImpl implements UserDao {

	@PersistenceContext
	private EntityManager em;

	private static Logger logger = Logger.getLogger(PasswordEncryption.class);

	@Override
	public long checkIfEmailAlreadyExists(String emailId) {
		logger.debug("checkIfEmailAlreadyExists starts..");
		Query query = em.createQuery("SELECT u FROM User u WHERE u.emailId = :emailId");
		query.setParameter("emailId", emailId);
		logger.debug("checkIfEmailAlreadyExists ends..");

		return query.getResultStream().count();
	}

	@Override
	public void updateUserDetails(User user) {
		logger.debug("updateUserDetails starts..");
		Query query = em.createQuery(
				"UPDATE User u SET u.firstName =:firstName, u.lastName =:lastName, u.middleName =:middleName"
						+ "u.contactNumber =:contactNumber, u.emailId =:emailId, u.status =:status, u.createdBy =:createdBy, u.createdOn =:createdOn,"
						+ "u.  modifiedOn =:modifiedOn, u.modifiedBy =:modifiedBy");
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

		logger.debug("updateUserDetails ends..");
		query.executeUpdate();
	}

	@Override
	@Transactional
	public void updateStatusOfUserByUserId(Long userId, Status status) {
		logger.debug("updateStatusOfUserByUserId starts..");
		Query query = em.createQuery("UPDATE User u SET u.status = :status WHERE u.id = :userId");
		query.setParameter("status", status);
		query.setParameter("userId", userId);

		em.joinTransaction();
		query.executeUpdate();
		logger.debug("updateStatusOfUserByUserId starts..");
	}
}
