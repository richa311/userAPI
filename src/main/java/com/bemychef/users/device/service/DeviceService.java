package com.bemychef.users.device.service;

import org.springframework.stereotype.Service;

import com.bemychef.users.device.model.dto.DeviceDTO;

@Service
public interface DeviceService {

	public boolean addDevice(DeviceDTO deviceDTO);

	DeviceDTO findDeviceById(Long deviceId);
}
