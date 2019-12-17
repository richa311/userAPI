package com.bemychef.users.user.service.impl;

import java.util.List;
import java.util.Optional;

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

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserBinder userBinder;

	private static Logger logger = Logger.getLogger(PasswordEncryption.class);

	/**
	 * Saves user
	 * 
	 * @param user
	 *            object
	 */
	public User register(User user) {
		logger.debug("Register users method starts..");
		String encryptedPassword = PasswordEncryption.encrypt(user.getPassword());
		user.setPassword(encryptedPassword);
		logger.debug("Register users method ends..");
		return userRepository.save(user);
	}

	/**
	 * Validates if user is new or is already present by emailID
	 *
	 * @param emailId
	 */
	public boolean isUserAlreadyPresent(String emailId) {
		logger.debug("Executing isUserAlreadyPresent..");
		return checkIfUserWithGivenEmailExists(emailId);
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
	public boolean updateStatusByUserId(Long userId, String status) {
		logger.debug("updateStatusByUserId starts...");
		Status enumStatus = null;
		if (status.equals("Active")) {
			enumStatus = Status.ACTIVE;
		} else if (status.equalsIgnoreCase("Inactive")) {
			enumStatus = Status.INACTIVE;
		} else if (status.equalsIgnoreCase("Deleted")) {
			enumStatus = Status.DELETED;
		}
		return userDao.updateStatusOfUserByUserId(userId, enumStatus);
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
	public String getEmailIdByUserId(long id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if(optionalUser.isPresent()) {
			return optionalUser.get().getEmailId();
		}else {
			return null;
		}
		
	}
	
	public UserBinder getUserBinder() {
		return userBinder;
	}

	public void setUserBinder(UserBinder userBinder) {
		this.userBinder = userBinder;
	}
}
