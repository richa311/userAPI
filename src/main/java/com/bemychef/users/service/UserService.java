package com.bemychef.users.service;

import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.bemychef.users.dto.UserDTO;

/**
 * Service class for registration
 */
@Service
public interface UserService {

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
	Response getUserDetails();

	/**
	 *
	 * @param userId
	 * @return user by userId
	 */
	Response getUserDetailsById(Long userId);

	/**
	 * gets user's status by userId
	 * 
	 * @param userId
	 * @return
	 */
	Response getUserStatus(Long userId);

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

	Response registerUser(UserDTO userDTO);
}
