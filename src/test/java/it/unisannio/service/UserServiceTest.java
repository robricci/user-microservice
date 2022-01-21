package it.unisannio.service;

import it.unisannio.controller.dto.LoginDTO;
import it.unisannio.controller.dto.RegisterDTO;
import it.unisannio.controller.dto.SessionDTO;
import it.unisannio.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    private String username1, password1;
    private String username2, password2, firstName, lastName, email;

    @BeforeEach
    public void setup() {
        username1 = "passenger";
        password1 = "password";
        username2 = "passenger2";
        password2 = "password";
        firstName = "Mario";
        lastName = "Rossi";
        email = "mrossi@email.it";

    }

    @Test
    void loginUser() {
        LoginDTO loginDTO = new LoginDTO(username1, password1);
        SessionDTO sessionDTO = userService.loginUser(loginDTO);
        assertTrue(sessionDTO.getRoles().get(0).equals("ROLE_PASSENGER"));
        assertTrue(sessionDTO.getJwt() != null);

    }



    @Test
    void getUserWithAuthorities() {
        assertTrue(true);
    }


    @Test
    void registerUser() {
        RegisterDTO registerDTO = new RegisterDTO(username2, password2, firstName, lastName, email);
        SessionDTO sessionDTO2 = userService.registerUser(registerDTO);

        assertTrue(sessionDTO2.getRoles().get(0).equals("ROLE_PASSENGER"));
        assertNotNull(sessionDTO2.getJwt());

    }

    @Test
    void validateToken() {
        LoginDTO loginDTO = new LoginDTO(username1, password1);
        SessionDTO sessionDTO = userService.loginUser(loginDTO);
        SessionDTO sessionDTO1 = userService.validateToken(sessionDTO.getJwt());

        assertSame(sessionDTO.getJwt(), sessionDTO1.getJwt());


    }
}