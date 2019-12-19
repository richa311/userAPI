package com.bemychef.users.dto;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.bemychef.users.model.User;

public class DeviceDTO {

	@NotNull
	private String deviceType;
	private Long DeviceToken;
	@NotNull
	@ManyToOne
	private User user;

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public Long getDeviceToken() {
		return DeviceToken;
	}

	public void setDeviceToken(Long deviceToken) {
		DeviceToken = deviceToken;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
