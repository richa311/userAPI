package com.bemychef.users.constants;

import com.bemychef.users.util.PropertiesUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorConstants implements Serializable {

    CONTACT_ADMIN("U50000", ""), INVALID_EMAIL("U50003", ""), INVALID_FIRST_NAME("U50001", ""),
    INVALID_LAST_NAME("U50002", ""), EITHER_MOBILE_OR_EMAIL("U50004", ""), EMAIL_ALREADY_EXISTS("U50005", ""),
    USER_NOT_FOUND("U50006", "");

    private final String errorCode;
    private final String errorMessage;

    ErrorConstants(final String errorCode, final String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return PropertiesUtil.getDataByKey(this.name());
    }

    @JsonCreator
    static ErrorConstants findValue(@JsonProperty("errorCode") String errorCode, @JsonProperty("errorMessage") String errorMessage) {

        return Arrays.stream(ErrorConstants.values()).filter(v -> v.errorCode == errorCode && v.errorMessage.equals(errorMessage)).findFirst().get();
    }

}