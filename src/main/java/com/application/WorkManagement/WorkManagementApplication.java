package com.application.WorkManagement;

import com.application.WorkManagement.security.key.RSAKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RSAKeyProperties.class)
public class WorkManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkManagementApplication.class, args);
	}

}
