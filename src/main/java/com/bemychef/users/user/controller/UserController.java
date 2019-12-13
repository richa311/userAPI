package com.bemychef.users.user.controller;

import com.bemychef.users.user.model.User;
import com.bemychef.users.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * user registration controller, for routing the APIs
 */
@RestController
@RequestMapping(path = "/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/user",consumes = "application/json")
    public int registerUser(@RequestBody User user) {
         userService.register(user);
         return Response.Status.CREATED.getStatusCode();
    }

    @PostMapping(path = "/user/validate", consumes = "application/json", produces = "application/json")
    public void validateUserByEmail(@RequestBody String emailId) {
        userService.validateUserByEmail(emailId);
    }

    @GetMapping(path = "/users", consumes = "application/json", produces = "application/json")
    public List<User> getUsers(){
        return userService.getUserDetails();
    }

    @GetMapping(path = "/users/{userId}", consumes = "application/json", produces = "application/json")
    public User getUserDetailsById(@PathVariable Long userId){
        return userService.getUserDetailsById(userId);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
