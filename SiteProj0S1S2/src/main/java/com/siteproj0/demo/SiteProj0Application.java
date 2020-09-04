package com.siteproj0.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SiteProj0Application {

	public static void main(String[] args) {
		SpringApplication.run(SiteProj0Application.class, args);
	}
}
