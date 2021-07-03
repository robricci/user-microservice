package it.unisannio;

import it.unisannio.controller.AuthenticationController;
import it.unisannio.controller.TicketController;
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
        register(AuthenticationController.class);
        register(TicketController.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
