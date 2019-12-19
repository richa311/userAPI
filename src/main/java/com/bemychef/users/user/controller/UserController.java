package com.bemychef.users.user.controller;

import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bemychef.users.user.binder.UserBinder;
import com.bemychef.users.user.model.Status;
import com.bemychef.users.user.model.User;
import com.bemychef.users.user.model.dto.UserDTO;
import com.bemychef.users.user.service.UserService;
import com.bemychef.users.verification.service.ConfirmUserService;

/**
 * user registration controller, for routing the APIs
 */
@RestController
@RequestMapping(path = "/api")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserBinder userBinder;

	@Autowired
	private ConfirmUserService confirmUserService;

	/**
	 * Takes userDTO as param and checks: if the user is already present - returns
	 * Http FOUND status code. if the user is not present, then binds it to user
	 * object and and then registers the user and then sends out confirmation for
	 * eMail and sends out Http CREATED status code.
	 * 
	 * @param userDTO
	 * @return status code
	 */
	@PostMapping(path = "/user", consumes = "application/json")
	public int registerUser(@RequestBody UserDTO userDTO) {
		if (!userService.isUserAlreadyPresent(userDTO.getEmailId())) {
			return Response.Status.FOUND.getStatusCode();
		} else {
			User user = userBinder.bindUserDTOToUser(userDTO);
			userService.register(user);
			confirmUserService.confirmUser(user);
			return Response.Status.CREATED.getStatusCode();
		}
	}

	/**
	 * This API validates user by email. It checks if the user is already present or
	 * not.
	 * 
	 * @param emailId
	 * @return Http Status code
	 */
	@PostMapping(path = "/user/validate", consumes = "application/json", produces = "application/json")
	public int validateUserByEmail(@RequestBody String emailId) {
		if (userService.isUserAlreadyPresent(emailId))
			return Response.Status.FOUND.getStatusCode();
		else
			return Response.Status.NOT_FOUND.getStatusCode();
	}

	/**
	 * @return list of all users
	 */
	@GetMapping(path = "/users", consumes = "application/json", produces = "application/json")
	public List<User> getUsers() {
		return userService.getUserDetails();
	}

	/**
	 * 
	 * @param userId
	 * @return user which belongs to the passed userId
	 */
	@GetMapping(path = "/users/{userId}", consumes = "application/json", produces = "application/json")
	public UserDTO getUserDetailsById(@PathVariable Long userId) {
		return userService.getUserDetailsById(userId);
	}

	/**
	 * 
	 * @param userId
	 * @return user status by user id
	 */
	@GetMapping(path = "/status/{userId}")
	public Status getUserStatus(@PathVariable Long userId) {
		return userService.getUserStatus(userId);
	}

	/**
	 * This API updates the user's status
	 * 
	 * @param userId
	 * @param status
	 * @return Http status code
	 */
	@PostMapping(path = "status/{userId}")
	public int updateUserStatus(@PathVariable Long userId, @RequestBody String status) {
		if (userService.updateStatusByUserId(userId, status))
			return Response.Status.ACCEPTED.getStatusCode();
		else
			return Response.Status.NOT_MODIFIED.getStatusCode();
	}

	/**
	 * This API updates user details.
	 * 
	 * @param userId
	 * @param userDTO
	 */
	@PostMapping(path = "users/{userId}")
	public void updateUserDetails(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
		userService.updateDetails(userId, userDTO);
	}

	/**
	 * This API confirms the user by verifying the token
	 * 
	 * @param confirmationToken
	 * @return HTTP status code
	 */
	@GetMapping(path = "/confirm-account")
	public int confirmUserAccount(@RequestParam("token") String confirmationToken) {
		return confirmUserService.verifyUserByToken(confirmationToken);
	}

	// getter setters
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserBinder getUserBinder() {
		return userBinder;
	}

	public void setUserBinder(UserBinder userBinder) {
		this.userBinder = userBinder;
	}
}
