package it.unisannio.controller;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.unisannio.controller.dto.SessionDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import it.unisannio.jwt.JWTFilter;
import it.unisannio.jwt.TokenProvider;
import it.unisannio.controller.dto.JWTTokenDTO;
import it.unisannio.controller.dto.LoginDTO;

import java.util.stream.Collectors;

import static it.unisannio.SecurityUtils.resolveToken;


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
@Path("/users")
public class AuthenticationRestController {

	private final TokenProvider tokenProvider;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;

	public AuthenticationRestController(TokenProvider tokenProvider,
			AuthenticationManagerBuilder authenticationManagerBuilder) {
		this.tokenProvider = tokenProvider;
		this.authenticationManagerBuilder = authenticationManagerBuilder;
	}

	@POST
	@Path("/login")
	public Response login(@Valid LoginDTO loginDto) {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				loginDto.getUsername(), loginDto.getPassword());

		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.createToken(authentication);

		return Response.ok(new JWTTokenDTO(jwt))
				.header(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt)
				.build();
	}

	@POST
	@Path("/register")
	public Response register(@Valid LoginDTO loginDto) {

		return Response.ok().build();
	}

	@POST
	@Path("/forgot-password")
	public Response forgotPassword(@Valid LoginDTO loginDto) {

		return Response.ok().build();
	}

	@POST
	@Path("/validate-session")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response validateSession(String token) {
		SessionDTO session = new SessionDTO();
		String jwt = resolveToken(token);
		if (tokenProvider.validateToken(jwt)) {
			Authentication authentication = tokenProvider.getAuthentication(jwt);
			session.setAuthenticated(true);
			session.setRoles(authentication.getAuthorities().stream()
					.map(GrantedAuthority::getAuthority)
					.collect(Collectors.toList()));
		}
		return Response.ok(session).build();
	}

}
