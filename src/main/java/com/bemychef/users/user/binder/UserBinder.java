package com.bemychef.users.user.binder;

import javax.inject.Named;

import com.bemychef.users.user.model.User;
import com.bemychef.users.user.model.dto.UserDTO;

@Named("userBinderBean")
public class UserBinder {

	public User bindUserDTOToUser(UserDTO userDTO) {
		User user = new User();
		user.setFirstName(userDTO.getFirstName());
		user.setMiddleName(userDTO.getMiddleName());
		user.setLastName(userDTO.getLastName());
		user.setContactNumber(userDTO.getContactNumber());
		user.setCreatedBy(userDTO.getCreatedBy());
		user.setCreatedOn(userDTO.getCreatedOn());
		user.setEmailId(userDTO.getEmailId());
		user.setId(userDTO.getId());
		user.setModifiedBy(userDTO.getModifiedBy());
		user.setModifiedOn(userDTO.getModifiedOn());
		user.setPassword(userDTO.getPassword());
		user.setStatus(userDTO.getStatus());
		
		return user;
	}
	
	public UserDTO bindUserToUserDTO(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setFirstName(user.getFirstName());
		userDTO.setMiddleName(user.getMiddleName());
		userDTO.setLastName(user.getLastName());
		userDTO.setContactNumber(user.getContactNumber());
		userDTO.setCreatedBy(user.getCreatedBy());
		userDTO.setCreatedOn(user.getCreatedOn());
		userDTO.setEmailId(user.getEmailId());
		userDTO.setId(user.getId());
		userDTO.setModifiedBy(user.getModifiedBy());
		userDTO.setModifiedOn(user.getModifiedOn());
		userDTO.setPassword(user.getPassword());
		userDTO.setStatus(user.getStatus());
		
		return userDTO;
	}
}
