package com.bemychef.users.exceptions;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"field", "responseMap"})
public class ResponseInfo {

	/**
	 * Error code for success: U5XXXX, Error code for errors: U6XXXX
	 * 
	 */
	private String field;
	private Map<String, String> responseMap = new HashMap<>();

	public ResponseInfo(String field, Map<String, String> responseMap) {
		this.field = field;
		this.setResponseMap(responseMap);
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Map<String, String> getResponseMap() {
		return responseMap;
	}

	public void setResponseMap(Map<String, String> responseMap) {
		this.responseMap = responseMap;
	}
}
