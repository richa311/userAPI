package com.bemychef.users.dao;

import javax.inject.Named;

@Named("ConfirmationDaoBean")
public interface ConfirmationTokenDao {

	Object findByToken(String confirmationToken);

}
