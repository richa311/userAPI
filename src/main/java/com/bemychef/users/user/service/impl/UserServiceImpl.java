package com.bemychef.users.user.service.impl;

import java.awt.geom.RectangularShape;
import java.util.List;
import java.util.Optional;

import com.bemychef.users.exceptions.ErrorInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bemychef.users.security.PasswordEncryption;
import com.bemychef.users.user.binder.UserBinder;
import com.bemychef.users.user.dao.UserDao;
import com.bemychef.users.user.dao.UserRepository;
import com.bemychef.users.user.model.Status;
import com.bemychef.users.user.model.User;
import com.bemychef.users.user.model.dto.UserDTO;
import com.bemychef.users.user.service.UserService;

import javax.ws.rs.core.Response;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserBinder userBinder;

	private static Logger logger = Logger.getLogger(UserServiceImpl.class);

	/**
	 * Saves user
	 * 
	 * @param user
	 *            object
	 */
	public User register(User user) {
		logger.debug("Register users method starts..");
		String encryptedPassword = "";
		User returnUser = null;
		try {
			encryptedPassword = PasswordEncryption.encrypt(user.getPassword());
			user.setPassword(encryptedPassword);
			returnUser = userRepository.save(user);
			logger.debug("Register users method ends..");
		}catch (Exception ex){
			logger.error("Caught exception while saving user details : "+ ex.toString());
			return null;
		}
		return returnUser;
	}

	/**
	 * Validates if user is new or is already present by emailID
	 *
	 * @param emailId
	 */
	public Response isUserAlreadyPresent(String emailId) {
		logger.debug("Executing isUserAlreadyPresent..");
		if(checkIfUserWithGivenEmailExists(emailId)){
			return Response.status(Response.Status.FOUND).build();
		}else{
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	/**
	 * updates user details
	 * 
	 * @param userDTO
	 */
	@Override
	public void updateDetails(Long userId, UserDTO userDTO) {
		logger.debug("updateDetails starts...");
		User user = userBinder.bindUserDTOToUser(userDTO);
		logger.debug("updateDetails ends...");
		userDao.updateUserDetails(user);
	}

	/**
	 *
	 * @return gets list of all users
	 */
	@Override
	public List<User> getUserDetails() {
		logger.debug("Executing getUserDetails...");
		return (List<User>) userRepository.findAll();
	}

	/**
	 * @param userId
	 * @return gets user by it's userId
	 */
	@Override
	public UserDTO getUserDetailsById(Long userId) {
		logger.debug("getUserDetailsById starts...");
		Optional<User> userOptional = userRepository.findById(userId);
		logger.debug("getUserDetailsById ends...");
		if (userOptional.isPresent())
			return userBinder.bindUserToUserDTO(userOptional.get());
		else
			return null;
	}

	@Override
	public Status getUserStatus(Long userId) {
		logger.debug("getUserStatus starts...");
		Optional<User> optionalUser = userRepository.findById(userId);
		logger.debug("getUserStatus ends...");
		if (optionalUser.isPresent()) {
			return optionalUser.get().getStatus();
		} else
			return null;
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
		}catch(Exception ex){
			ErrorInfo errorInfo = new ErrorInfo();
			errorInfo.setCode(Response.Status.INTERNAL_SERVER_ERROR.toString());
			errorInfo.setField(null);
			errorInfo.setMessage("Some error occurred, Please contact your administration...");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorInfo).build();
		}
	}

	/**
	 * Query emailID to DB and check if user already exists
	 *
	 * @param emailId
	 * @return true/false : if email is already present/not present
	 */
	private boolean checkIfUserWithGivenEmailExists(String emailId) {
		logger.debug("Executing checkIfUserWithGivenEmailExists...");
		return userDao.checkIfEmailAlreadyExists(emailId) == 0;
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
		}catch (Exception ex){
			logger.error("Got exception while getting email by user Id :" + ex.toString());
			ErrorInfo errorInfo = new ErrorInfo();
			errorInfo.setCode(Response.Status.INTERNAL_SERVER_ERROR.toString());
			errorInfo.setField(null);
			errorInfo.setMessage("Some error occurred, Please contact your administration...");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorInfo).build();
		}
	}
	
	public UserBinder getUserBinder() {
		return userBinder;
	}

	public void setUserBinder(UserBinder userBinder) {
		this.userBinder = userBinder;
	}
}
