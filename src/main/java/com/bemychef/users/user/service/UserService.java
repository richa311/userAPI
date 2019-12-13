package com.bemychef.users.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bemychef.users.user.model.User;
import com.bemychef.users.user.model.dto.UserDTO;

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
    int validateUserByEmail(String emailId);

    /**
     * Updates user's registration details
     *
     * @param user
     */
    void updateDetails(User user);

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
}
