package com.mitrais.chipper.temankondangan.be.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.mitrais.chipper.temankondangan.be.authservice.config.AppProperties;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableEurekaClient //for making this application as eureka client
@EnableDiscoveryClient
@SpringBootApplication
@EnableSwagger2
@EnableConfigurationProperties(AppProperties.class)
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

}
