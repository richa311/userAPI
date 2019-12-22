package com.bemychef.users.service.impl;

import javax.inject.Named;
import javax.ws.rs.core.Response;

import com.bemychef.users.constants.ErrorConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.bemychef.users.dao.ConfirmationTokenDao;
import com.bemychef.users.dao.ConfirmationTokenRepository;
import com.bemychef.users.model.ConfirmationToken;
import com.bemychef.users.model.User;
import com.bemychef.users.service.ConfirmUserService;
import com.bemychef.users.service.EmailService;
import com.bemychef.users.service.UserService;

@Service
@Named(value = "confirmationUserServiceImpl")
public class ConfirmUserServiceImpl implements ConfirmUserService {

	@Autowired
	private UserService userService;
	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;
	@Autowired
	private EmailService emailSenderService;
	@Autowired
	private ConfirmationTokenDao confirmationTokenDao;

	private static Logger logger = LoggerFactory.getLogger(ConfirmUserServiceImpl.class);
	@Override
	public Response confirmUser(User user) {
		ConfirmationToken token = new ConfirmationToken(user);
		try {
			sendUserEmail(user, token);
			return Response.status(Response.Status.ACCEPTED).build();
		} catch (Exception ex) {
			logger.error("Exception occurred during saving confirmation token : " + ex.toString());
			return returnResponseUponException();
		}
	}

	private void sendUserEmail(User user, ConfirmationToken token) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("saurabhguest8899@gmail.com");
		mailMessage.setTo(user.getEmailId());
		mailMessage.setSubject("Be My Chef: Registration");
		mailMessage.setText("To confirm your account, please click here : "
				+ "http://localhost:8080/v1/api/confirm-account?token=" + token.getToken());
		emailSenderService.sendEmail(mailMessage);
	}

	@Override
	public Response verifyUserByToken(String confirmationToken) {
		ConfirmationToken confirmationTokenObj = (ConfirmationToken) confirmationTokenDao
				.findByToken(confirmationToken);
		if (null != confirmationTokenObj) {
			try {
				activateUser(confirmationTokenObj);
				return Response.status(Response.Status.ACCEPTED).build();
			} catch (Exception ex) {
				logger.error("Exception occurred while verifying token for user : " + ex.toString());
				return returnResponseUponException();
			}
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	private Response returnResponseUponException() {
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorConstants.CONTACT_ADMIN).build();
	}

	private Response activateUser(ConfirmationToken confirmationTokenObj) {
		return userService.updateStatusByUserId(confirmationTokenObj.getUser().getId(), "Active");
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ConfirmationTokenRepository getConfirmationTokenRepository() {
		return confirmationTokenRepository;
	}

	public void setConfirmationTokenRepository(ConfirmationTokenRepository confirmationTokenRepository) {
		this.confirmationTokenRepository = confirmationTokenRepository;
	}

	public ConfirmationTokenDao getConfirmationTokenDao() {
		return confirmationTokenDao;
	}

	public void setConfirmationTokenDao(ConfirmationTokenDao confirmationTokenDao) {
		this.confirmationTokenDao = confirmationTokenDao;
	}
}
