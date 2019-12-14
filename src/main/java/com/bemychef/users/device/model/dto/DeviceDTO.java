package com.bemychef.users.device.model.dto;

import com.bemychef.users.user.model.User;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

public class DeviceDTO {

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
