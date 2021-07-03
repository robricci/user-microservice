package it.unisannio.controller;

import it.unisannio.controller.dto.*;
import it.unisannio.service.TicketService;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RestController
@Path("/tickets")
public class TicketController {

	private final TicketService ticketService;

	public TicketController(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@GET
	public Response getOneTimeTicket(@Context HttpServletRequest request) {
		TicketDTO ticket = this.ticketService.generateOneTimeTicket(request);
		return Response.ok(ticket).build();
	}

	@GET
	@Path("/{ott}/validate")
	public Response validateOneTimeTicket(@PathParam(value = "ott") String ott, @Context HttpServletRequest request) {
		boolean isValidTicket = this.ticketService.validateOneTimeTicket(ott, request);
		Response.ResponseBuilder responseBuilder = isValidTicket
				? Response.status(Response.Status.ACCEPTED).entity(true)
				: Response.status(Response.Status.NOT_ACCEPTABLE).entity(false);
		return responseBuilder.build();
	}
}
