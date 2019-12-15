package com.bemychef.users.user.dao;

import org.springframework.stereotype.Repository;

import com.bemychef.users.user.model.Status;
import com.bemychef.users.user.model.User;

@Repository
public interface UserDao {

	long checkIfEmailAlreadyExists(String emailId);

	void updateUserDetails(User user);

	boolean updateStatusOfUserByUserId(Long userId, Status status);
}
