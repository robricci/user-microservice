package it.unisannio.controller;

import it.unisannio.controller.dto.LoginDTO;
import it.unisannio.controller.dto.RegisterDTO;
import it.unisannio.controller.dto.SessionDTO;
import org.apache.http.HttpEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
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

        // BlackBox Testing
        // Login passenger user
        LoginDTO loginDTO = new LoginDTO("passenger", "password");
        Response response = authenticationController.login(loginDTO);
        assertTrue(response.getStatus()==200);
        SessionDTO sessionDTO = (SessionDTO) response.getEntity();
        assertTrue(sessionDTO.getJwt()!= null);
        assertTrue(sessionDTO.getRoles().get(0).equals("ROLE_PASSENGER"));

        // BlackBox Testing
        //Login driver user
        loginDTO = new LoginDTO("driver", "password");
        response = authenticationController.login(loginDTO);
        assertTrue(response.getStatus()==200);
        sessionDTO = (SessionDTO) response.getEntity();
        assertTrue(sessionDTO.getJwt()!= null);
        assertTrue(sessionDTO.getRoles().get(0).equals("ROLE_DRIVER"));

        //WhiteBox Testing
        //Login with bad credentials
        loginDTO.setUsername("pass");
        loginDTO.setPassword("1234");
        response = authenticationController.login(loginDTO);
        assertTrue(response.getStatus()==401);



    }

    @Test
    void register() {

        // BlackBox Testing
        RegisterDTO registerDTO = new RegisterDTO("robricci", "12345678", "roberto", "ricci", "rob@gmail.com");
        Response response = authenticationController.register(registerDTO);
        SessionDTO sessionDTO = (SessionDTO) response.getEntity();
        assertTrue(response.getStatus()==200);
        assertTrue(sessionDTO.getJwt()!=null);
        assertTrue(sessionDTO.getRoles().get(0).equals("ROLE_PASSENGER"));

        // Register new User with already existing username
        RegisterDTO registerDTO2 = new RegisterDTO("passenger", "12345678", "Luca", "Ricci", "lricci@gmail.com");
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            authenticationController.register(registerDTO);
        });


    }

    @Test
    void validateSession() {
        // BlackBox Testing
        LoginDTO loginDTO = new LoginDTO("passenger", "password");
        Response response =  authenticationController.login(loginDTO);
        SessionDTO sessionDTO = (SessionDTO) response.getEntity();
        assertNotNull(sessionDTO.getJwt());
        assertTrue(sessionDTO.getRoles().get(0).equals("ROLE_PASSENGER"));

        response = authenticationController.validateSession(sessionDTO.getJwt());
        assertTrue(response.getStatus()==200);

        // WhiteBox Testing
        sessionDTO.setJwt("");
        response = authenticationController.validateSession(sessionDTO.getJwt());
        assertTrue(response.getStatus()==401);

    }



}