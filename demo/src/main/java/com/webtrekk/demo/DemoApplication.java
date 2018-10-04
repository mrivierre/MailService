package com.webtrekk.demo;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.webtrekk.demo.consumer.ConsumerCreator;
import com.webtrekk.demo.service.IEMailService;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableAutoConfiguration
public class DemoApplication {

	@Autowired
	@Qualifier("eMailService")
	private IEMailService eMailService;

	private static IEMailService staticEMailService;

	@PostConstruct
	private void init() {
		staticEMailService = this.eMailService;
		
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		ConsumerCreator.runConsumer(staticEMailService);
	}
}
