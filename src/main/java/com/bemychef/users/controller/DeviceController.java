package com.bemychef.users.controller;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bemychef.users.dto.DeviceDTO;
import com.bemychef.users.service.DeviceService;

@RestController
@RequestMapping("/api")
public class DeviceController {

	@Autowired
	private DeviceService deviceService;

	@PostMapping("/device")
	public int addDevice(@RequestBody DeviceDTO deviceDTO) {
		if (deviceService.addDevice(deviceDTO))
			return Response.Status.CREATED.getStatusCode();
		else
			return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
	}

	@GetMapping
	public DeviceDTO getDeviceDetails(@PathVariable Long deviceId) {
		return deviceService.findDeviceById(deviceId);
	}

	public DeviceService getDeviceService() {
		return deviceService;
	}

	public void setDeviceService(DeviceService deviceService) {
		this.deviceService = deviceService;
	}

}
