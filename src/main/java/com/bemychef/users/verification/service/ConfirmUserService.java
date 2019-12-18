package com.bemychef.users.useraccount.service;

import org.springframework.stereotype.Service;

import com.bemychef.users.user.model.User;

@Service
public interface ConfirmUserService {

	void confirmUser(User user);

	int verifyUserByToken(String confirmationToken);

}
