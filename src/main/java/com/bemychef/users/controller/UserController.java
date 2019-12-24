package com.bemychef.users.controller;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bemychef.users.dto.UserDTO;
import com.bemychef.users.service.ConfirmUserService;
import com.bemychef.users.service.UserService;

/**
 * user registration controller, for routing the APIs
 */
@RestController
@RequestMapping(path = "/api")
public class UserController {

	@Autowired
	private UserService userService;
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
	@PostMapping(path = "/user", consumes = "application/json", produces = "application/json")
	public Response registerUser(@RequestBody UserDTO userDTO) throws JsonProcessingException {
		return userService.registerUser(userDTO);
	}

	/**
	 * This API validates user by email. It checks if the user is already present or
	 * not.
	 * 
	 * @param emailId
	 * @return Http Status code
	 */
	@PostMapping(path = "/user/validate", produces = "application/json")
	public Response validateUserByEmail(@RequestBody String emailId) {
		return userService.isUserAlreadyPresent(emailId);
	}

	/**
	 * @return list of all users
	 */
	@GetMapping(path = "/users", produces = "application/json")
	public Response getUsers() {
		return userService.getUserDetails();
	}

	/**
	 * 
	 * @param userId
	 * @return user which belongs to the passed userId
	 */
	@GetMapping(path = "/users/{userId}", produces = "application/json")
	public Response getUserDetailsById(@PathVariable Long userId) {
		return userService.getUserDetailsById(userId);
	}

	/**
	 * 
	 * @param userId
	 * @return user status by user id
	 */
	@GetMapping(path = "/status/{userId}")
	public Response getUserStatus(@PathVariable Long userId) {
		return userService.getUserStatus(userId);
	}

	/**
	 * This API updates the user's status
	 * 
	 * @param userId
	 * @param status
	 * @return Http status code
	 */
	@PostMapping(path = "status/{userId}", produces = "application/json")
	public Response updateUserStatus(@PathVariable Long userId, @RequestBody String status) {
		return userService.updateStatusByUserId(userId, status);
	}

	/**
	 * This API updates user details.
	 * 
	 * @param userId
	 * @param userDTO
	 */
	@PostMapping(path = "users/{userId}", consumes="application/json", produces = "application/json")
	public Response updateUserDetails(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
		return userService.updateDetails(userId, userDTO);
	}

	/**
	 * This API confirms the user by verifying the token
	 * 
	 * @param confirmationToken
	 * @return HTTP status code
	 */
	@GetMapping(path = "/confirm-account", produces = "application/json")
	public Response confirmUserAccount(@RequestParam("token") String confirmationToken) {
		return confirmUserService.verifyUserByToken(confirmationToken);
	}

	@GetMapping(path = "/users/counts", produces="application/json")
	public Response getCountOfActiveUsers(@QueryParam(value = "status") String status){
		return userService.getCountOfUsers(status);
	}

	// getter setters
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
