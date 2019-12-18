package com.bemychef.users.useraccount.service.impl;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.bemychef.users.email.service.EmailService;
import com.bemychef.users.user.model.User;
import com.bemychef.users.user.service.UserService;
import com.bemychef.users.useraccount.dao.ConfirmationTokenDao;
import com.bemychef.users.useraccount.dao.ConfirmationTokenRepository;
import com.bemychef.users.useraccount.model.ConfirmationToken;
import com.bemychef.users.useraccount.service.ConfirmUserService;

@Service
public class ConfirmUserServiceImpl implements ConfirmUserService{

	@Autowired
	private UserService userService;
	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;
	@Autowired
    private EmailService emailSenderService;
	@Autowired
	private ConfirmationTokenDao confirmationTokenDao;
	
	@Override
	public void confirmUser(User user) {
		ConfirmationToken token = new ConfirmationToken(user);
		confirmationTokenRepository.save(token);
		sendUserEmail(user, token);
		
	}

	private void sendUserEmail(User user, ConfirmationToken token) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("saurabhguest8899@gmail.com");
		mailMessage.setTo(user.getEmailId());
		mailMessage.setSubject("Be My Chef: Registration");
		mailMessage.setText("To confirm your account, please click here : "
	            +"http://localhost:8080/v1/api/confirm-account?token="+token.getToken());
		
		emailSenderService.sendEmail(mailMessage);
	}

	@Override
	public int verifyUserByToken(String confirmationToken) {
		if(confirmationTokenDao.findByToken(confirmationToken) != null) {
			return Response.Status.ACCEPTED.getStatusCode();
		}else {
			return Response.Status.NOT_FOUND.getStatusCode();
		}
		
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
