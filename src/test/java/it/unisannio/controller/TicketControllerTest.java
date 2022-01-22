package it.unisannio.controller;

import it.unisannio.controller.dto.LoginDTO;
import it.unisannio.controller.dto.SessionDTO;
import it.unisannio.controller.dto.TicketDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TicketControllerTest {

    @Autowired
    private TicketController ticketController;
    @Autowired
    private AuthenticationController authenticationController;

    @Test
    void getOneTimeTicket() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        LoginDTO loginDTO = new LoginDTO("passenger", "password");
        Response response = authenticationController.login(loginDTO);
        SessionDTO sessionDTO = (SessionDTO) response.getEntity();
        request.addHeader("Authorization", "Bearer " + sessionDTO.getJwt());
        response = ticketController.getOneTimeTicket(request);

        assertTrue(response.getStatus()==200);


    }

    @Test
    void validateOneTimeTicket() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        LoginDTO loginDTO = new LoginDTO("passenger", "password");
        Response response = authenticationController.login(loginDTO);
        SessionDTO sessionDTO = (SessionDTO) response.getEntity();
        request.addHeader("Authorization", "Bearer " + sessionDTO.getJwt());
        response = ticketController.getOneTimeTicket(request);
        TicketDTO ticketDTO = (TicketDTO) response.getEntity();
        response = ticketController.validateOneTimeTicket(ticketDTO.getOneTimeTicket(), request);

        assertEquals(response.getStatus(), 202);

    }
}