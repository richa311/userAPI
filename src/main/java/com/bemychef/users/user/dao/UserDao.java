package com.bemychef.users.user.dao;

import com.bemychef.users.user.model.Status;
import com.bemychef.users.user.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

    long checkIfEmailAlreadyExists(String emailId);

    void updateUserDetails(User user);
}
