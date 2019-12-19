package com.bemychef.users.verification.dao;

import javax.inject.Named;

@Named("ConfirmationDaoBean")
public interface ConfirmationTokenDao {

	Object findByToken(String confirmationToken);

}
