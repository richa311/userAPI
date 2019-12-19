package com.bemychef.users.binder;

import java.sql.Timestamp;
import java.util.Date;

import javax.inject.Named;

import com.bemychef.users.dto.UserDTO;
import com.bemychef.users.model.User;

@Named("userBinderBean")
public class UserBinder {

	public User bindUserDTOToUser(UserDTO userDTO) {
		User user = new User();
		user.setFirstName(userDTO.getFirstName());
		user.setMiddleName(userDTO.getMiddleName());
		user.setLastName(userDTO.getLastName());
		user.setContactNumber(userDTO.getContactNumber());
		user.setEmailId(userDTO.getEmailId());
		user.setId(userDTO.getId());
		user.setStatus(userDTO.getStatus());
		user.setCreatedBy(userDTO.getEmailId());
		user.setCreatedOn(getTimeStamp());
		user.setModifiedBy(userDTO.getEmailId());
		user.setModifiedOn(getTimeStamp());
		return user;
	}
	
	private Timestamp getTimeStamp() {
		Date date = new Date();
		Long time = date.getTime();
		return new Timestamp(time);
	}

	public UserDTO bindUserToUserDTO(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setFirstName(user.getFirstName());
		userDTO.setMiddleName(user.getMiddleName());
		userDTO.setLastName(user.getLastName());
		userDTO.setContactNumber(user.getContactNumber());
		userDTO.setEmailId(user.getEmailId());
		userDTO.setId(user.getId());
		userDTO.setStatus(user.getStatus());
		
		return userDTO;
	}
}
