package com.bemychef.users.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource({"classpath:MessageConstant.properties"})
public class PropertiesUtil {

	public PropertiesUtil() {
		// empty constructor
	}

	@Autowired
	private Environment env;

	private static Environment environment;

	@PostConstruct
	public void init() {
		environment = env;
	}

	public static String getProperty(String propertyKey) {
		return environment.getProperty(propertyKey);
	}
}
