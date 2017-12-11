package com.okta.poc.iot.tisensor.apidynamodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableAutoConfiguration
@EnableSwagger2
@ComponentScan(basePackages = {"com.okta.poc.iot.tisensor.apidynamodb"})
@EntityScan(basePackages = {"com.okta.poc.iot.tisensor.apidynamodb"})  // scan JPA entitiessh

public class OktaAPIWWWTiSensorTag {

	public static void main(String[] args) {
		SpringApplication.run(OktaAPIWWWTiSensorTag.class, args);
	}
}
