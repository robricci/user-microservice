package it.unisannio.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import it.unisannio.controller.dto.LoginDTO;
import it.unisannio.controller.dto.RegisterDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.IsNull.notNullValue;



@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceIntegrationTest {


    private LoginDTO loginDTO1;
    private LoginDTO loginDTO2;
    private RegisterDTO registerDTO1;
    private RegisterDTO registerDTO2;

    @Before
    public void setup() {
        String username1 = "passenger2";
        String password1 = "password";
        String firstName1 = "Mario";
        String lastName1 = "Rossi";
        String email1 = "mrossi@email.it";

        String username2 = "driver";
        String password2 = "password";
        String firstName2 = "Luca";
        String lastName2 = "Bianchi";
        String email2 = "lbianchi@email.it";


        registerDTO1 = new RegisterDTO();
        registerDTO1.setUsername(username1);
        registerDTO1.setPassword(password1);
        registerDTO1.setFirstname(firstName1);
        registerDTO1.setLastname(lastName1);
        registerDTO1.setEmail(email1);

        registerDTO2 = new RegisterDTO();
        registerDTO2.setUsername(username2);
        registerDTO2.setPassword(password2);
        registerDTO2.setFirstname(firstName2);
        registerDTO2.setLastname(lastName2);
        registerDTO2.setEmail(email2);

        //Enable RestAssured log
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    public void login() throws Exception {
        loginDTO1 = new LoginDTO("passenger", "password");
        loginDTO2 = new LoginDTO("robricci", "password");

        // Login registered User
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        given()
                .contentType("application/json")
                .body(asJsonString(loginDTO1))
        .when()
                .post("http://localhost:9091/api/users/login")
        .then()
                .statusCode(200)
        .assertThat()
                .body("jwt", notNullValue())
                .body("roles", contains("ROLE_PASSENGER"));


        // Login unregistered User - Bad credential Exception
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        given()
                .contentType("application/json")
                .body(asJsonString(loginDTO2))
        .when()
                .post("http://localhost:9091/api/users/login")
        .then()
                .statusCode(401);


        }

    @Test
    public void register() throws Exception {

     //Register new User
     RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
     given()
             .contentType("application/json")
             .body(asJsonString(registerDTO1))
    .when()
             .post("http://localhost:9091/api/users/register")
    .then()
             .statusCode(200)
     .assertThat()
                .body("jwt", notNullValue())
                .body("roles", contains("ROLE_PASSENGER"));

     // Register new User with already existing username
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        given()
                .contentType("application/json")
                .body(asJsonString(registerDTO2))
        .when()
                .post("http://localhost:9091/api/users/register")
        .then()
                .statusCode(500);

    }


    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}