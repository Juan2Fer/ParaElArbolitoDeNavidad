package com.students.students;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient // Se registra en el servidor de descubrimiento (Eureka)
@SpringBootApplication
public class StudentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentsApplication.class, args);
	}

}
