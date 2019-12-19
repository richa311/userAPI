package com.bemychef.users.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.bemychef.users.exceptions.ErrorInfo;
import com.bemychef.users.service.ConfirmUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bemychef.users.security.PasswordEncryption;
import com.bemychef.users.binder.UserBinder;
import com.bemychef.users.dao.UserDao;
import com.bemychef.users.dao.UserRepository;
import com.bemychef.users.model.Status;
import com.bemychef.users.model.User;
import com.bemychef.users.dto.UserDTO;
import com.bemychef.users.service.UserService;
import com.bemychef.users.util.PropertiesUtil;

import javax.ws.rs.core.Response;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserBinder userBinder;
	@Autowired
	private ConfirmUserService confirmUserService;
	@Autowired
	private PropertiesUtil propertiesUtil;

	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	private static final String CHEF_USER_CONTACT_ADMIN = "chef.user.contact.admin";
	private static final String CHEF_USER_EMAIL_EXIST = "chef.user.email.exist";

	private User register(User user) {
		logger.debug("Register users method starts..");
		String encryptedPassword = "";
		User returnUser = null;
		try {
			encryptedPassword = PasswordEncryption.encrypt(user.getPassword());
			user.setPassword(encryptedPassword);
			returnUser = userRepository.save(user);
			logger.debug("Register users method ends..");
		} catch (Exception ex) {
			logger.error("Caught exception while saving user details : " + ex.toString());
			return null;
		}
		returnUser.setPassword("");
		return returnUser;
	}

	@Override
	public Response isUserAlreadyPresent(String emailId) {
		logger.debug("Executing isUserAlreadyPresent..");
		if (checkIfUserWithGivenEmailExists(emailId)) {
			return Response.status(Response.Status.FOUND).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@Override
	public Response updateDetails(Long userId, UserDTO userDTO) {
		logger.debug("updateDetails starts...");
		User user = null;
		try {
			user = userBinder.bindUserDTOToUser(userDTO);
			userDao.updateUserDetails(user);
		} catch (Exception ex) {
			ErrorInfo errorInfo = new ErrorInfo(Response.Status.INTERNAL_SERVER_ERROR.toString(), null,
					PropertiesUtil.getProperty(CHEF_USER_CONTACT_ADMIN));
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorInfo).build();
		}
		return Response.status(Response.Status.ACCEPTED).entity(user).build();
	}

	@Override
	public List<UserDTO> getUserDetails() {
		logger.debug("Executing getUserDetails...");
		List<User> users = (List<User>) userRepository.findAll();
		List<UserDTO> userDTOList = new ArrayList<>();
		users.forEach(user -> userDTOList.add(userBinder.bindUserToUserDTO(user)));
		return userDTOList;
	}

	@Override
	public UserDTO getUserDetailsById(Long userId) {
		logger.debug("getUserDetailsById starts...");
		Optional<User> userOptional = userRepository.findById(userId);
		logger.debug("getUserDetailsById ends...");
		if (userOptional.isPresent())
			return userBinder.bindUserToUserDTO(userOptional.get());
		else {
			return null;
		}
	}

	@Override
	public Status getUserStatus(Long userId) {
		logger.debug("getUserStatus starts...");
		Optional<User> optionalUser = userRepository.findById(userId);
		logger.debug("getUserStatus ends...");
		if (optionalUser.isPresent()) {
			return optionalUser.get().getStatus();
		} else {
			return null;
		}
	}

	@Override
	public Response updateStatusByUserId(Long userId, String status) {
		logger.debug("updateStatusByUserId starts...");
		Status enumStatus = null;
		if (status.equals("Active")) {
			enumStatus = Status.ACTIVE;
		} else if (status.equalsIgnoreCase("Inactive")) {
			enumStatus = Status.INACTIVE;
		} else if (status.equalsIgnoreCase("Deleted")) {
			enumStatus = Status.DELETED;
		}
		try {
			userDao.updateStatusOfUserByUserId(userId, enumStatus);
			return Response.status(Response.Status.ACCEPTED).build();
		} catch (Exception ex) {
			ErrorInfo errorInfo = new ErrorInfo(Response.Status.INTERNAL_SERVER_ERROR.toString(), null,
					PropertiesUtil.getProperty(CHEF_USER_CONTACT_ADMIN));
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorInfo).build();
		}
	}

	private boolean checkIfUserWithGivenEmailExists(String emailId) {
		logger.debug("Executing checkIfUserWithGivenEmailExists...");
		return userDao.checkIfEmailAlreadyExists(emailId) != 0;
	}

	@Override
	public Response getEmailIdByUserId(long id) {
		try {
			Optional<User> optionalUser = userRepository.findById(id);
			if (optionalUser.isPresent()) {
				return Response.status(Response.Status.FOUND).entity(optionalUser.get().getEmailId()).build();
			} else {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		} catch (Exception ex) {
			logger.error("Got exception while getting email by user Id :" + ex.toString());
			ErrorInfo errorInfo = new ErrorInfo(Response.Status.INTERNAL_SERVER_ERROR.toString(), null,
					PropertiesUtil.getProperty(CHEF_USER_CONTACT_ADMIN));
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorInfo).build();
		}
	}

	@Override
	public Response registerUser(UserDTO userDTO) {
		if (checkIfUserWithGivenEmailExists(userDTO.getEmailId())) {
			ErrorInfo errorInfo = new ErrorInfo(Response.Status.FOUND.toString(), null,
					PropertiesUtil.getProperty(CHEF_USER_EMAIL_EXIST));
			return Response.status(Response.Status.FOUND).entity(errorInfo).build();
		} else {
			char[] password = userDTO.getPassword();
			User user = userBinder.bindUserDTOToUser(userDTO);
			user.setPassword(Arrays.toString(password));
			register(user);
			confirmUserService.confirmUser(user);
			return Response.status(Response.Status.CREATED).entity(user).build();
		}
	}

	public UserBinder getUserBinder() {
		return userBinder;
	}

	public void setUserBinder(UserBinder userBinder) {
		this.userBinder = userBinder;
	}

	public PropertiesUtil getPropertiesUtil() {
		return propertiesUtil;
	}

	public void setPropertiesUtil(PropertiesUtil propertiesUtil) {
		this.propertiesUtil = propertiesUtil;
	}
}
