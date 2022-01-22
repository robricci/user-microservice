package it.unisannio.controller;

import it.unisannio.controller.dto.LoginDTO;
import it.unisannio.controller.dto.RegisterDTO;
import it.unisannio.controller.dto.SessionDTO;
import org.apache.http.HttpEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AuthenticationControllerTest {

    @Autowired
    private AuthenticationController authenticationController;

    @Test
    void login() {
        LoginDTO loginDTO = new LoginDTO("passenger", "password");
        Response response = authenticationController.login(loginDTO);
        assertTrue(response.getStatus()==200);

        //bad credentials
        loginDTO.setUsername("pass");
        loginDTO.setPassword("aaa");
        response = authenticationController.login(loginDTO);
        assertTrue(response.getStatus()==401);

    }

    @Test
    void register() {
        RegisterDTO registerDTO = new RegisterDTO("robricci", "12345678", "roberto", "ricci", "rob@gmail.com");
        Response response = authenticationController.register(registerDTO);
        assertTrue(response.getStatus()==200);


    }

    @Test
    void validateSession() {
        LoginDTO loginDTO = new LoginDTO("passenger", "password");
        Response response =  authenticationController.login(loginDTO);
        SessionDTO sessionDTO = (SessionDTO) response.getEntity();
        System.out.println("-------JWT-------\n" + sessionDTO.getJwt());
        response = authenticationController.validateSession(sessionDTO.getJwt());
        assertTrue(response.getStatus()==200);
    }

}