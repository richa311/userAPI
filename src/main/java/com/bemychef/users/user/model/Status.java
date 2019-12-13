package com.bemychef.users.user.model;

public enum Status {

    ACTIVE("Active"), INACTIVE("InActive"), DELETED("Deleted");

    private String statusValue;

    private Status(String status){
        this.statusValue = status;
    }
    public String getStatusValue() {
        return statusValue;
    }
}
