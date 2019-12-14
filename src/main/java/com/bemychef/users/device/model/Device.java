package com.bemychef.users.device.model;

import com.bemychef.users.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
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
