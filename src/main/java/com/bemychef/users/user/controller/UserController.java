package com.bemychef.users.user.controller;

import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bemychef.users.user.binder.UserBinder;
import com.bemychef.users.user.model.Status;
import com.bemychef.users.user.model.User;
import com.bemychef.users.user.model.dto.UserDTO;
import com.bemychef.users.user.service.UserService;

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

	@PostMapping(path = "/user", consumes = "application/json")
	public int registerUser(@RequestBody UserDTO userDTO) {
		if (!userService.isUserAlreadyPresent(userDTO.getEmailId())) {
			return Response.Status.FOUND.getStatusCode();
		} else {
			User user = userBinder.bindUserDTOToUser(userDTO);
			userService.register(user);
			return Response.Status.CREATED.getStatusCode();
		}
	}

	@PostMapping(path = "/user/validate", consumes = "application/json", produces = "application/json")
	public int validateUserByEmail(@RequestBody String emailId) {
		if (userService.isUserAlreadyPresent(emailId))
			return Response.Status.FOUND.getStatusCode();
		else
			return Response.Status.NOT_FOUND.getStatusCode();
	}

	@GetMapping(path = "/users", consumes = "application/json", produces = "application/json")
	public List<User> getUsers() {
		return userService.getUserDetails();
	}

	@GetMapping(path = "/users/{userId}", consumes = "application/json", produces = "application/json")
	public UserDTO getUserDetailsById(@PathVariable Long userId) {
		return userService.getUserDetailsById(userId);
	}

	@GetMapping(path = "/status/{userId}")
	public Status getUserStatus(@PathVariable Long userId) {
		return userService.getUserStatus(userId);
	}

	@PostMapping(path = "status/{userId}")
	public int updateUserStatus(@PathVariable Long userId, @RequestBody String status) {
		if (userService.updateStatusByUserId(userId, status))
			return Response.Status.ACCEPTED.getStatusCode();
		else
			return Response.Status.NOT_MODIFIED.getStatusCode();
	}

	@PostMapping(path = "users/{userId}")
	public void updateUserDetails(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
		userService.updateDetails(userId, userDTO);
	}

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
