package it.unisannio.controller;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.unisannio.controller.dto.RegisterDTO;
import it.unisannio.controller.dto.SessionDTO;
import it.unisannio.service.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import it.unisannio.jwt.JWTFilter;
import it.unisannio.controller.dto.JWTTokenDTO;
import it.unisannio.controller.dto.LoginDTO;


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
@Path("/users")
public class AuthenticationController {

	private final UserService userService;

	public AuthenticationController(UserService userService) {
		this.userService = userService;
	}

	@POST
	@Path("/login")
	public Response login(@Valid LoginDTO loginDto) {
		JWTTokenDTO jwtToken = null;
		try {
			jwtToken = this.userService.loginUser(loginDto);
		} catch (BadCredentialsException e) {
			System.out.println("Login '" + loginDto.getUsername() + "' BadCredentialsException");
		}
		return responseWithJwt(jwtToken);
	}

	@POST
	@Path("/register")
	public Response register(@Valid RegisterDTO registerDTO) {
		JWTTokenDTO jwtToken = null;
		try {
			jwtToken = this.userService.registerUser(registerDTO);
		} catch (BadCredentialsException e) {
			System.out.println("Register&Login '" + registerDTO.getUsername() + "' BadCredentialsException");
		}
		return responseWithJwt(jwtToken);
	}

	@POST
	@Path("/forgot-password")
	public Response forgotPassword() {
		// TODO
		return Response.ok().build();
	}

	@POST
	@Path("/validate-session")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response validateSession(String token) {
		SessionDTO session = this.userService.validateToken(token);
		return Response.ok(session).build();
	}

	private Response responseWithJwt(JWTTokenDTO jwtToken) {
		if (jwtToken != null)
			return Response.ok(jwtToken)
					.header(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwtToken.getJwt())
					.build();

		return Response.status(Response.Status.UNAUTHORIZED).build();
	}

}
