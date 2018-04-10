package com.djavid.check_server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChecksApplication {

	public static final Logger log = LoggerFactory.getLogger("bitcoinrate");

	public static void main(String[] args) {
		SpringApplication.run(ChecksApplication.class, args);
	}
}
