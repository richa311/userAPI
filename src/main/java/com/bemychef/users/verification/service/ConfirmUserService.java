package com.bemychef.users.verification.service;

import org.springframework.stereotype.Service;

import com.bemychef.users.user.model.User;

import javax.ws.rs.core.Response;

@Service
public interface ConfirmUserService {

	Response confirmUser(User user);

	Response verifyUserByToken(String confirmationToken);

}
