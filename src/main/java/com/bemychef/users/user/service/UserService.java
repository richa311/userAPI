package com.bemychef.users.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bemychef.users.user.model.Status;
import com.bemychef.users.user.model.User;
import com.bemychef.users.user.model.dto.UserDTO;

import javax.ws.rs.core.Response;

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
	Response isUserAlreadyPresent(String emailId);

	/**
	 * Updates user's registration details
	 *
	 * @param user
	 */
	Response updateDetails(Long userId, UserDTO userDTO);

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
	 * 
	 * @param userId
	 * @return
	 */
	Status getUserStatus(Long userId);

	/**
	 * updates user's status by userId
	 * 
	 * @param userId
	 * @param status
	 * @return
	 */
	Response updateStatusByUserId(Long userId, String status);

	/**
	 * gets email id by user id
	 * 
	 * @param id
	 */
	Response getEmailIdByUserId(long id);
}
