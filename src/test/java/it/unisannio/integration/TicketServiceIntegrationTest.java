package it.unisannio.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import it.unisannio.controller.dto.LoginDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class TicketServiceIntegrationTest {

    private LoginDTO loginDTO1, loginDTO2;


    @Before
    public void setup() {
        //Enable RestAssured log
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        loginDTO1 = new LoginDTO("passenger", "password");
        loginDTO2 = new LoginDTO("driver", "password");

    }

    @Test
    public void getOneTimeTicket() {

        //Login to extract jwt
        String jwt =
        given()
                .contentType("application/json")
                .body(asJsonString(loginDTO1))
        .when()
                .post("http://localhost:9091/api/users/login")
        .then()
                //.statusCode(200)
        .extract()
                .path("jwt");

        //Get One Time Ticket
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer "+jwt)
        .when()
                .get("http://localhost:9091/api/tickets")
        .then()
                .statusCode(200)
        .assertThat()
                .body("oneTimeTicket", notNullValue());

    }

    @Test
    public void validateOneTimeTicket() {
        //Login to extract jwt
        String jwt =
                given()
                        .contentType("application/json")
                        .body(asJsonString(loginDTO2))
                .when()
                        .post("http://localhost:9091/api/users/login")
                .then()
                        .extract()
                        .path("jwt");

        //Extract ott
        String ott =
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer "+jwt)
        .when()
                .get("http://localhost:9091/api/tickets")
        .then()
                .statusCode(200)
        .extract()
                .path("oneTimeTicket");

        //Validate ott
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer "+jwt)
       .when()
                .get("http://localhost:9091/api/tickets/{ott}/validate",ott)
      .then()
                .statusCode(202)
       .assertThat()
                .body(equalTo("true"));



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
