package com.bemychef.users.dao.impl;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.bemychef.users.dao.ConfirmationTokenDao;

@Named("confirmationDaoImplBean")
public class ConfirmationTokenDaoImpl implements ConfirmationTokenDao {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Object findByToken(String confirmationToken) {
		Query query = em.createQuery("SELECT ct FROM ConfirmationToken ct WHERE ct.token =: token"); 
		query.setParameter("token", confirmationToken);
		
		return query.getSingleResult();
	}

	
}
