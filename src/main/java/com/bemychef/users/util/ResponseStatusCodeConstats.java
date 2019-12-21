package com.bemychef.users.util;

public enum ResponseStatusCodeConstats {

	CONTACT_ADMIN("U50000"), INVALID_EMAILID("U50003"), INVALID_FIRST_NAME("U50001"), INVALID_LAST_NAME("U50002"), 
	EITHER_MOBILE_OR_EMAIL("U50004"), EMAIL_ALREADY_EXISTS("U50005"), USER_NOT_FOUND("U50006");

	private String statusCode;

	private ResponseStatusCodeConstats(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusCode() {
		return statusCode;
	}
}
