package com.lmc.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages= {"com.lmc.springcloud"})
@ComponentScan("com.lmc.springcloud")
public class DeptConsumer80_Feign_App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(DeptConsumer80_Feign_App.class, args);

	}

}
