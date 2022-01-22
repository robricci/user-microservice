package it.unisannio.service;

import it.unisannio.controller.dto.LoginDTO;
import it.unisannio.controller.dto.SessionDTO;
import it.unisannio.controller.dto.TicketDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TicketServiceTest {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserService userService;
    private String username1, password1;

    @BeforeEach
    public void setup() {
        username1 = "passenger";
        password1 = "password";
    }



    @Test
    void generateOneTimeTicket() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        LoginDTO loginDTO = new LoginDTO(username1, password1);
        SessionDTO sessionDTO = userService.loginUser(loginDTO);
        sessionDTO = userService.validateToken(sessionDTO.getJwt());
        request.addHeader("Authorization", "Bearer " + sessionDTO.getJwt());
        TicketDTO ticketDTO = ticketService.generateOneTimeTicket(request);
        System.out.println("---TICKET------");
        System.out.println(ticketDTO.getOneTimeTicket());
        assert(ticketDTO.getOneTimeTicket()!=null);

    }

    @Test
    void validateOneTimeTicket() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        LoginDTO loginDTO = new LoginDTO(username1, password1);
        SessionDTO sessionDTO = userService.loginUser(loginDTO);
        sessionDTO = userService.validateToken(sessionDTO.getJwt());
        request.addHeader("Authorization", "Bearer " + sessionDTO.getJwt());
        TicketDTO ticketDTO = ticketService.generateOneTimeTicket(request);
        boolean esito = ticketService.validateOneTimeTicket( ticketDTO.getOneTimeTicket(),request);
        assertTrue(esito);
    }
}