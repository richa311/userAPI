package com.bemychef.users.device.binder;

import com.bemychef.users.device.model.Device;
import com.bemychef.users.device.model.dto.DeviceDTO;

import javax.inject.Named;

@Named("DeviceBinderBean")
public class DeviceBinder {

    public Device bindDeviceDTOToDevice(DeviceDTO deviceDTO) {
        Device device = new Device();
        device.setDeviceToken(deviceDTO.getDeviceToken());
        device.setDeviceType(deviceDTO.getDeviceType());
        device.setUser(deviceDTO.getUser());
        return device;
    }

    public DeviceDTO bindDeviceToDeviceDTO(Device device){
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setDeviceToken(device.getDeviceToken());
        deviceDTO.setDeviceType(device.getDeviceType());
        deviceDTO.setUser(device.getUser());
        return deviceDTO;
    }
}
