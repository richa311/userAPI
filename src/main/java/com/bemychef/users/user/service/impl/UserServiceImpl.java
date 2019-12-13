package com.bemychef.users.user.service.impl;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bemychef.users.user.binder.UserBinder;
import com.bemychef.users.user.dao.UserDao;
import com.bemychef.users.user.dao.UserRepository;
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

    /**
     * Saves user
     * @param user object
     */
    public User register(User user) {
        return userRepository.save(user);
    }

    /**
     * Validates if user is new or is already present by emailID
     *
     * @param emailId
     */
    public int validateUserByEmail(String emailId) {
        if (checkIfUserWithGivenEmailExists(emailId)) {
            return Response.Status.FOUND.getStatusCode();
        } else
            return Response.Status.NOT_FOUND.getStatusCode();
    }

    /**
     * updates user details
     * @param user
     */
    @Override
    public void updateDetails(User user) {
        userDao.updateRegistrationDetails(user);
    }

    /**
     *
     * @return gets list of all users
     */
    @Override
    public List<User> getUserDetails() {
        return (List<User>) userRepository.findAll();
    }

    /**
     * @param userId
     * @return gets user by it's userId
     */
    @Override
    public UserDTO getUserDetailsById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent())
            return userBinder.bindUserToUserDTO(userOptional.get());
        else
        	return null;
    }

    /**
     * Query emailID to DB and check if user already exists
     *
     * @param emailId
     * @return true/false : if email is already present/not present
     */
    private boolean checkIfUserWithGivenEmailExists(String emailId) {
       return userDao.checkIfEmailAlreadyExists(emailId) == 0;
    }

	public UserBinder getUserBinder() {
		return userBinder;
	}

	public void setUserBinder(UserBinder userBinder) {
		this.userBinder = userBinder;
	}
}
