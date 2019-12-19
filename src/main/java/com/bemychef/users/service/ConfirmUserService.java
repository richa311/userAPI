package com.bemychef.users.service;

import org.springframework.stereotype.Service;

import com.bemychef.users.model.User;

import javax.ws.rs.core.Response;

@Service
public interface ConfirmUserService {

	Response confirmUser(User user);

	Response verifyUserByToken(String confirmationToken);

}
