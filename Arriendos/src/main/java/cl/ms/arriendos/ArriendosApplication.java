package cl.ms.arriendos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ArriendosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArriendosApplication.class, args);
	}

}
