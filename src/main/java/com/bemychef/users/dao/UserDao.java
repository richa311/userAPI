package com.bemychef.users.dao;

import org.springframework.stereotype.Repository;

import com.bemychef.users.constants.Status;
import com.bemychef.users.model.User;

@Repository
public interface UserDao {

	long checkIfEmailAlreadyExists(String emailId);

	void updateUserDetails(User user);

	void updateStatusOfUserByUserId(Long userId, Status status);
}
