package utp.edu.sistema_gestor_incidencias.security.filters;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utp.edu.sistema_gestor_incidencias.dto.login.LoginRequestDTO;
import utp.edu.sistema_gestor_incidencias.security.TokenJwtConfig;
import tools.jackson.databind.ObjectMapper;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	
	//por defecto /login la endpoint para iniciar sesion
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		LoginRequestDTO login = null;
		String username = null;
		String password = null;

		try {
			login = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDTO.class);
			username = login.getUsername();
			password = login.getPassword();
		} catch (Exception e) {
			e.printStackTrace();
		}

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);

		return authenticationManager.authenticate(authenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		User user = (User) authResult.getPrincipal();
		String username = user.getUsername();

		Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
		List<String> authorities = roles.stream()
				.map(GrantedAuthority::getAuthority)
				.toList();
		Claims claims = Jwts.claims().add("authorities", authorities).build();
		String token = Jwts.builder()
				.subject(username)
				.claims(claims)
				.signWith(TokenJwtConfig.SECRET_KEY)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 3600000))
				.compact();
		Map<String, String> body = new HashMap<>();
		body.put("token", token);
		body.put("message", String.format("%s has iniciado session con exito", username));

		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.addHeader(TokenJwtConfig.HEADER_AUTHORIZATION, TokenJwtConfig.PREFIX_TOKEN + token);
		response.setContentType(TokenJwtConfig.CONTENT_TYPE);
		response.setStatus(200);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		Map<String, String> body = new HashMap<>();
		body.put("message", "Error en la autenticacion, username or password incorrectos");
		body.put("error", failed.getMessage());

		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setContentType(TokenJwtConfig.CONTENT_TYPE);
		response.setStatus(401);
	}

}
