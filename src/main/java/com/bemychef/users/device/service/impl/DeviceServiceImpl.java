package com.bemychef.users.device.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bemychef.users.device.binder.DeviceBinder;
import com.bemychef.users.device.dao.DeviceRepository;
import com.bemychef.users.device.model.Device;
import com.bemychef.users.device.model.dto.DeviceDTO;
import com.bemychef.users.device.service.DeviceService;

@Service
public class DeviceServiceImpl implements DeviceService {

	@Autowired
	private DeviceRepository deviceRepository;
	@Autowired
	private DeviceBinder binder;

	@Override
	public boolean addDevice(DeviceDTO deviceDTO) {
		try {
			Device device = binder.bindDeviceDTOToDevice(deviceDTO);
			deviceRepository.save(device);
			return true;
		} catch (Exception e) {
			return false;
			// log exception
		}
	}

	@Override
	public DeviceDTO findDeviceById(Long deviceId) {
		Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
		if (optionalDevice.isPresent()) {
			return binder.bindDeviceToDeviceDTO(optionalDevice.get());
		} else {
			return null;
		}
	}
}
