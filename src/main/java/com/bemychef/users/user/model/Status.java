package com.bemychef.users.user.model;

public enum Status {

	ACTIVE("Active"), INACTIVE("InActive"), DELETED("Deleted");

	private String statusValue;

	private Status(String status) {
		this.statusValue = status;
	}

	public String getStatusValue() {
		return statusValue;
	}

	@Override
	public String toString() {
		switch (this) {
		case ACTIVE:
			return "Active";
		case INACTIVE:
			return "Inactive";
		case DELETED:
			return "Deleted";
		}
		return null;
	}
}
