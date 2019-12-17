package com.bemychef.users.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bemychef.users.user.model.Status;
import com.bemychef.users.user.model.User;
import com.bemychef.users.user.model.dto.UserDTO;

/**
 * Service class for registration
 */
@Service
public interface UserService {

	/**
	 * Registers users based on registration form
	 *
	 * @param user
	 * @return
	 */
	User register(User user);

	/**
	 * Validates if user is new or is already present by emailID
	 *
	 * @param emailId
	 */
	boolean isUserAlreadyPresent(String emailId);

	/**
	 * Updates user's registration details
	 *
	 * @param user
	 */
	void updateDetails(Long userId, UserDTO userDTO);

	/**
	 *
	 * @return List of Users
	 */
	List<User> getUserDetails();

	/**
	 *
	 * @param userId
	 * @return user by userId
	 */
	UserDTO getUserDetailsById(Long userId);

	/**
	 * gets user's status by userId
	 * @param userId
	 * @return
	 */
	Status getUserStatus(Long userId);

	/**
	 * updates user's status by userId
	 * @param userId
	 * @param status
	 * @return
	 */
	boolean updateStatusByUserId(Long userId, String status);

	/**
	 * gets email id by user id
	 * @param id
	 */
	String getEmailIdByUserId(long id);
}
