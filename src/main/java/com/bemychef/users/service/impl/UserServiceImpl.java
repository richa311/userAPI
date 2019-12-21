package com.bemychef.users.service.impl;

import com.bemychef.users.binder.UserBinder;
import com.bemychef.users.constants.ResponseStatusCodeConstants;
import com.bemychef.users.constants.Status;
import com.bemychef.users.dao.UserDao;
import com.bemychef.users.dao.UserRepository;
import com.bemychef.users.dto.UserDTO;
import com.bemychef.users.exceptions.ResponseInfo;
import com.bemychef.users.model.User;
import com.bemychef.users.security.PasswordEncryption;
import com.bemychef.users.service.ConfirmUserService;
import com.bemychef.users.service.UserService;
import com.bemychef.users.util.PropertiesUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.*;
import java.util.regex.Pattern;

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

    @Override
    public Response isUserAlreadyPresent(String emailId) {
        logger.debug("Executing isUserAlreadyPresent..");
        if (Objects.nonNull(checkIfUserWithGivenEmailExists(emailId)) &&
                Objects.nonNull(checkIfUserWithGivenEmailExists(emailId).getStatus()) &&
                checkIfUserWithGivenEmailExists(emailId).getStatus().equals(Status.ACTIVE)) {
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put(ResponseStatusCodeConstants.EMAIL_ALREADY_EXISTS.getStatusCode(),
                    PropertiesUtil.getProperty(ResponseStatusCodeConstants.EMAIL_ALREADY_EXISTS.getStatusCode()));
            ResponseInfo responseInfo = new ResponseInfo(emailId, responseMap);
            return Response.status(Response.Status.OK).entity(responseInfo).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity(emailId).build();
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
        return Response.status(Response.Status.ACCEPTED).entity(userDTO).build();
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
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put(ResponseStatusCodeConstants.USER_NOT_FOUND.getStatusCode(),
                    ResponseStatusCodeConstants.USER_NOT_FOUND.getStatusCode());
            ResponseInfo responseInfo = new ResponseInfo(userId.toString(), responseMap);
            return Response.status(Response.Status.OK).entity(responseInfo).build();
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
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put(ResponseStatusCodeConstants.USER_NOT_FOUND.getStatusCode(),
                    ResponseStatusCodeConstants.USER_NOT_FOUND.getStatusCode());
            ResponseInfo responseInfo = new ResponseInfo(userId.toString(), responseMap);
            return Response.status(Response.Status.OK).entity(responseInfo).build();
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
                return Response.status(Response.Status.FOUND).entity(optionalUser.get().getEmailId()).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (Exception ex) {
            logger.error("Got exception while getting email by user Id :" + ex.toString());
            return returnResponseUponException();
        }
    }

    @Override
    public Response registerUser(UserDTO userDTO) {
        Map<String, String> responseMap = validateUser(userDTO);
        if (!responseMap.isEmpty()) {
            ResponseInfo responseInfo = new ResponseInfo(null, responseMap);
            return Response.status(Response.Status.OK).entity(responseInfo).build();
        } else {
            if (Objects.nonNull(userDTO.getEmailId())) {
                User user = checkIfUserWithGivenEmailExists(userDTO.getEmailId());
                if (Objects.nonNull(user) && Objects.nonNull(user.getStatus()) && user.getStatus().equals(Status.ACTIVE)) {
                    responseMap.put(ResponseStatusCodeConstants.EMAIL_ALREADY_EXISTS.getStatusCode(), PropertiesUtil
                            .getProperty(ResponseStatusCodeConstants.EMAIL_ALREADY_EXISTS.getStatusCode()));
                    ResponseInfo responseInfo = new ResponseInfo(null, responseMap);
                    return Response.status(Response.Status.OK).entity(responseInfo).build();
                } else if (Objects.nonNull(user) && Objects.nonNull(user.getStatus()) &&
                        (user.getStatus().equals(Status.INACTIVE) || user.getStatus().equals(Status.DELETED))) {
                    confirmUserService.confirmUser(user);
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
        return Response.status(Response.Status.CREATED).entity(userBinder.bindUserToUserDTO(user)).build();
    }

    private Map<String, String> validateUser(UserDTO userDTO) {
        Map<String, String> responseMap = new HashMap<>();
        if (Objects.isNull(userDTO.getEmailId()) && Objects.isNull(userDTO.getContactNumber())) {
            responseMap.put(ResponseStatusCodeConstants.EITHER_MOBILE_OR_EMAIL.getStatusCode(),
                    PropertiesUtil.getProperty(ResponseStatusCodeConstants.EITHER_MOBILE_OR_EMAIL.getStatusCode()));
        }
        if (responseMap.isEmpty() && Objects.isNull(userDTO.getFirstName())) {
            responseMap.put(ResponseStatusCodeConstants.INVALID_FIRST_NAME.getStatusCode(),
                    PropertiesUtil.getProperty(ResponseStatusCodeConstants.INVALID_FIRST_NAME.getStatusCode()));
        }
        if (responseMap.isEmpty() && Objects.isNull(userDTO.getLastName())) {
            responseMap.put(ResponseStatusCodeConstants.INVALID_LAST_NAME.getStatusCode(),
                    PropertiesUtil.getProperty(ResponseStatusCodeConstants.INVALID_LAST_NAME.getStatusCode()));
        }
        if (responseMap.isEmpty() && !validateEmail(userDTO.getEmailId())) {
            responseMap.put(ResponseStatusCodeConstants.INVALID_EMAILID.getStatusCode(),
                    PropertiesUtil.getProperty(ResponseStatusCodeConstants.INVALID_EMAILID.getStatusCode()));
        }

        return responseMap;
    }

    private Response returnResponseUponException() {
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put(ResponseStatusCodeConstants.CONTACT_ADMIN.getStatusCode(),
                PropertiesUtil.getProperty(ResponseStatusCodeConstants.CONTACT_ADMIN.getStatusCode()));
        ResponseInfo responseInfo = new ResponseInfo(null, responseMap);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseInfo).build();
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
