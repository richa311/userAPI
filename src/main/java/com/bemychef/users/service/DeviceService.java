package com.bemychef.users.service;

import org.springframework.stereotype.Service;

import com.bemychef.users.dto.DeviceDTO;

@Service
public interface DeviceService {

	public boolean addDevice(DeviceDTO deviceDTO);

	DeviceDTO findDeviceById(Long deviceId);
}
