package com.example.mutualssl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MutualsslServerApplication {

	static {
		System.setProperty("javax.net.debug", "ssl");
	}

	public static void main(String[] args) {
		SpringApplication.run(MutualsslServerApplication.class, args);
	}
}
