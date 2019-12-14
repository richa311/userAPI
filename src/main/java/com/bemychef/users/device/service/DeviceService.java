package com.bemychef.users.device.service;

import com.bemychef.users.device.model.Device;
import com.bemychef.users.device.model.dto.DeviceDTO;
import org.springframework.stereotype.Service;

@Service
public interface DeviceService {

    public boolean addDevice(DeviceDTO deviceDTO);

    DeviceDTO findDeviceById(Long deviceId);
}
