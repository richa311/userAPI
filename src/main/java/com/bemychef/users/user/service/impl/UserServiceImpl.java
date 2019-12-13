package com.bemychef.users.user.service.impl;

import com.bemychef.users.user.dao.UserDao;
import com.bemychef.users.user.dao.UserRepository;
import com.bemychef.users.user.model.User;
import com.bemychef.users.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Service

public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRepository userRepository;

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
            return Response.Status.OK.getStatusCode();
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
    public User getUserDetailsById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent())
            return userOptional.get();
        else
            return userOptional.orElseThrow(IllegalArgumentException::new);
    }

    /**
     * Query emailID to DB and check if user already exists
     *
     * @param emailId
     * @return true/false : if email is already present/not present
     */
    private boolean checkIfUserWithGivenEmailExists(String emailId) {
        if(userDao.checkIfEmailAlreadyExists(emailId) == 0)
            return false;
        else
            return true;
    }
}
