package it.unisannio;

import it.unisannio.controller.AuthenticationRestController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import javax.ws.rs.ApplicationPath;

@SpringBootApplication
@EnableEurekaClient
@ApplicationPath("/api")
public class UserApplication extends ResourceConfig {

    public UserApplication() {
        register(AuthenticationRestController.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
