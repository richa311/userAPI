package com.bemychef.users.service.impl;

import com.bemychef.users.binder.UserBinder;
import com.bemychef.users.constants.ErrorConstants;
import com.bemychef.users.constants.Status;
import com.bemychef.users.dao.UserDao;
import com.bemychef.users.dao.UserRepository;
import com.bemychef.users.dto.UserDTO;
import com.bemychef.users.model.User;
import com.bemychef.users.security.PasswordEncryption;
import com.bemychef.users.service.ConfirmUserService;
import com.bemychef.users.service.UserService;
import com.bemychef.users.util.PropertiesUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public Response isUserAlreadyPresent(String emailId) {
        logger.debug("Executing isUserAlreadyPresent..");
        if (Objects.nonNull(checkIfUserWithGivenEmailExists(emailId)) &&
                Objects.nonNull(checkIfUserWithGivenEmailExists(emailId).getStatus()) &&
                checkIfUserWithGivenEmailExists(emailId).getStatus().equals(Status.ACTIVE)) {
            return Response.status(Response.Status.OK).entity(ErrorConstants.EMAIL_ALREADY_EXISTS).build();
        } else {
            return Response.status(Response.Status.OK).entity(ErrorConstants.USER_NOT_FOUND).build();
        }
    }

    @Override
    public Response updateDetails(Long userId, UserDTO userDTO) {
        logger.debug("updateDetails starts...");
        User user;
        try {
            user = userBinder.bindUserDTOToUser(userDTO);
            userDao.updateUserDetails(user);
        } catch (Exception ex) {
            return returnResponseUponException();
        }
        return Response.status(Response.Status.OK).entity(userDTO).build();
    }

    @Override
    public Response getUserDetails() {
        logger.debug("Executing getUserDetails...");
        List<User> users = (List<User>) userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        users.forEach(user -> userDTOList.add(userBinder.bindUserToUserDTO(user)));
        return Response.status(Response.Status.OK).entity(userDTOList).build();
    }

    @Override
    public Response getUserDetailsById(Long userId) {
        logger.debug("getUserDetailsById starts...");
        Optional<User> userOptional = userRepository.findById(userId);
        logger.debug("getUserDetailsById ends...");
        if (userOptional.isPresent())
            return Response.status(Response.Status.OK).entity(userBinder.bindUserToUserDTO(userOptional.get())).build();
        else {
            return Response.status(Response.Status.OK).entity(ErrorConstants.USER_NOT_FOUND).build();
        }
    }

    @Override
    public Response getUserStatus(Long userId) {
        logger.debug("getUserStatus starts...");
        Optional<User> optionalUser = userRepository.findById(userId);
        logger.debug("getUserStatus ends...");
        if (optionalUser.isPresent()) {
            return Response.status(Response.Status.OK).entity(optionalUser.get().getStatus()).build();
        } else {
            return Response.status(Response.Status.OK).entity(ErrorConstants.USER_NOT_FOUND).build();
        }
    }

    @Override
    public Response updateStatusByUserId(Long userId, String status) {
        logger.debug("updateStatusByUserId starts...");
        Status enumStatus = null;
        if (status.equalsIgnoreCase("Active")) {
            enumStatus = Status.ACTIVE;
        } else if (status.equalsIgnoreCase("Inactive")) {
            enumStatus = Status.INACTIVE;
        } else if (status.equalsIgnoreCase("Deleted")) {
            enumStatus = Status.DELETED;
        }
        try {
            userDao.updateStatusOfUserByUserId(userId, enumStatus);
            return Response.status(Response.Status.OK).build();
        } catch (Exception ex) {
            return returnResponseUponException();
        }
    }

    private User checkIfUserWithGivenEmailExists(String emailId) {
        logger.debug("Executing checkIfUserWithGivenEmailExists...");
        return userDao.checkIfEmailAlreadyExists(emailId);
    }

    @Override
    public Response getEmailIdByUserId(long id) {
        try {
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                return Response.status(Response.Status.OK).entity(optionalUser.get().getEmailId()).build();
            } else {
                return Response.status(Response.Status.OK).entity(ErrorConstants.USER_NOT_FOUND).build();
            }
        } catch (Exception ex) {
            logger.error("Got exception while getting email by user Id :" + ex.toString());
            return returnResponseUponException();
        }
    }

    @Override
    public Response registerUser(UserDTO userDTO) throws JsonProcessingException {
        List<Response> responseList = validateUser(userDTO);
        if (!responseList.isEmpty()) {
            return responseList.get(0);
        } else {
            if (Objects.nonNull(userDTO.getEmailId())) {
                User user = checkIfUserWithGivenEmailExists(userDTO.getEmailId());
                if (Objects.nonNull(user) && Objects.nonNull(user.getStatus()) && user.getStatus().equals(Status.ACTIVE)) {
                    return Response.status(Response.Status.OK).entity(ErrorConstants.EMAIL_ALREADY_EXISTS).build();
                } else if (Objects.nonNull(user) && Objects.nonNull(user.getStatus()) &&
                        (user.getStatus().equals(Status.INACTIVE) || user.getStatus().equals(Status.DELETED))) {
                    if (confirmUserService.confirmUser(user).getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
                        return returnResponseUponException();
                    }
                    UserDTO userDto = userBinder.bindUserToUserDTO(user);
                    return Response.status(Response.Status.OK).entity(userDto).build();
                } else {
                    return registerAndConfirmUser(userDTO);
                }
            } else if (Objects.nonNull(userDTO.getContactNumber())) {
                // code for mobile verification
            }
        }
        return null;
    }

    private Response registerAndConfirmUser(UserDTO userDTO) {
        char[] password = userDTO.getPassword();
        User user = userBinder.bindUserDTOToUser(userDTO);
        user.setPassword(Arrays.toString(password));
        if (Objects.nonNull(register(user))) {
            if (confirmUserService.confirmUser(user).getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
                return returnResponseUponException();
            }
        } else {
            return returnResponseUponException();
        }
        return Response.status(Response.Status.OK).entity(userBinder.bindUserToUserDTO(user)).build();
    }

    private List<Response> validateUser(UserDTO userDTO) throws JsonProcessingException {
        List<Response> responseList = new ArrayList<>();
        if (Objects.isNull(userDTO.getEmailId()) && Objects.isNull(userDTO.getContactNumber())) {
            responseList.add(Response.status(Response.Status.OK).entity(ErrorConstants.EITHER_MOBILE_OR_EMAIL).build());
        }
        if (responseList.isEmpty() && Objects.isNull(userDTO.getFirstName())) {
            responseList.add(Response.status(Response.Status.OK).entity(ErrorConstants.INVALID_FIRST_NAME).build());
        }
        if (responseList.isEmpty() && Objects.isNull(userDTO.getLastName())) {
            responseList.add(Response.status(Response.Status.OK).entity(ErrorConstants.INVALID_LAST_NAME).build());
        }
        if (responseList.isEmpty() && !validateEmail(userDTO.getEmailId())) {
            responseList.add(Response.status(Response.Status.OK).entity(ErrorConstants.INVALID_EMAIL).build());
        }
        return responseList;
    }

    private Response returnResponseUponException() {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorConstants.CONTACT_ADMIN).build();
    }

    private User register(User user) {
        logger.debug("Register users method starts..");
        String encryptedPassword = "";
        User returnUser = null;
        try {
            encryptedPassword = PasswordEncryption.encrypt(user.getPassword());
            user.setPassword(encryptedPassword);
            user.setStatus(Status.INACTIVE);
            returnUser = userRepository.save(user);
            logger.debug("Register users method ends..");
        } catch (Exception ex) {
            logger.error("Caught exception while saving user details : " + ex.toString());
            return null;
        }
        returnUser.setPassword("");
        return returnUser;
    }

    private boolean validateEmail(String emailId) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(emailId).matches();
    }

    @Override
    public Response getCountOfUsers(String status) {
        List<User> userList = (List<User>) userRepository.findAll();
        if(status.equalsIgnoreCase("all")){
            return Response.status(Response.Status.OK).entity(userList.size()).build();
        }else {
            return Response.status(Response.Status.OK).entity(userList.stream().filter(user -> user.getStatus().toString().equalsIgnoreCase(status)).collect(Collectors.toList()).size()).build();
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
