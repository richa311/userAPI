package com.bemychef.users.user.service;

import com.bemychef.users.user.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

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
    User getUserDetailsById(Long userId);
}
