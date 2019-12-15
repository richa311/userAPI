package com.bemychef.users.address.model;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

	private String flatNo;
	private String addressLine1;
	private String getAddressLine2;
	private String landmark;

	public String getFlatNo() {
		return flatNo;
	}

	public void setFlatNo(String flatNo) {
		this.flatNo = flatNo;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getGetAddressLine2() {
		return getAddressLine2;
	}

	public void setGetAddressLine2(String getAddressLine2) {
		this.getAddressLine2 = getAddressLine2;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}
}
