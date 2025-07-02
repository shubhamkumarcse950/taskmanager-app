package com.taskManagement.jwtFilter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.taskManagement.ServiceImpl.JwtUserDetails;
import com.taskManagement.jwtHelper.JwtHelper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

	private final JwtHelper jwtHelper;
	private final JwtUserDetails userDetails;

	public JwtFilter(JwtHelper jwtHelper, JwtUserDetails userDetails) {
		super();
		this.jwtHelper = jwtHelper;
		this.userDetails = userDetails;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String requestUri = request.getRequestURI();

		if (requestUri.startsWith("/user/register") || requestUri.startsWith("/user/login")) {
			filterChain.doFilter(request, response);
			return;
		}

		String reqestHeader = request.getHeader("Authorization");
		log.info("Header : [{}]", reqestHeader);
		String username = null;
		String token = null;

		if (reqestHeader != null && reqestHeader.startsWith("Bearer")) {
			token = reqestHeader.substring(7);
			try {
				username = jwtHelper.getUsernameFromToken(token);

			} catch (IllegalArgumentException e) {
				log.error("Illegal Argument while fetching the username !!", e);
			} catch (ExpiredJwtException e) {
				log.error("Given JWT token is expired !!", e);
			} catch (MalformedJwtException e) {
				log.error("The token is malformed or has been altered !!", e);
			} catch (Exception e) {
				log.error("Token validation failed due to an unexpected error", e);
			}
		} else {
			log.info("Invalid Header Value !!");
		}

		// Proceed if username is extracted and SecurityContext is empty
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			try {
				UserDetails userDetails = this.userDetails.loadUserByUsername(username);
				boolean validToken = this.jwtHelper.validateToken(token, userDetails);

				if (validToken) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				} else {
					log.info("Token validation failed !!");
				}
			} catch (UsernameNotFoundException e) {
				log.error("User not found: {}", e.getMessage());
				// You can handle user not found scenario here
			} catch (JwtException e) {
				log.error("JWT validation failed: {}", e.getMessage());
				// Handle token validation failure here
			} catch (Exception e) {
				log.error("An unexpected error occurred during authentication", e);
				// Handle other exceptions
			}
		}

		// Continue the filter chain
		filterChain.doFilter(request, response);
	}

	//
	// @Override
	// protected void doFilterInternal(HttpServletRequest request,
	// HttpServletResponse response, FilterChain filterChain)
	// throws ServletException, IOException {
	// String reqestHeader = request.getHeader("Authorization");
	// log.info(" Header : [{}]", reqestHeader);
	// String username = null;
	// String token = null;
	//
	// if (reqestHeader != null && reqestHeader.startsWith("Bearer")) {
	// token = reqestHeader.substring(7);
	// try {
	// username = jwtHelper.getUsernameFromToken(token);
	//
	// } catch (IllegalArgumentException e) {
	// log.info("Illegal Argument while fetching the username !!");
	// e.printStackTrace();
	// } catch (ExpiredJwtException e) {
	// log.info("Given jwt token is expired !!");
	// e.printStackTrace();
	// } catch (MalformedJwtException e) {
	// log.info("Some changed has done in token !! Invalid Token");
	// e.printStackTrace();
	// } catch (Exception e) {
	// log.error("token not valid ");
	// e.printStackTrace();
	// }
	//
	// } else {
	// log.info("Invalid Header Value !! ");
	// }
	// if (username != null &&
	// SecurityContextHolder.getContext().getAuthentication() == null) {
	// try {
	// UserDetails userDetails = this.userDetails.loadUserByUsername(username);
	// boolean validToken = this.jwtHelper.validateToken(token, userDetails);
	// if (validToken) {
	// UsernamePasswordAuthenticationToken authentication = new
	// UsernamePasswordAuthenticationToken(
	// userDetails, null, userDetails.getAuthorities());
	// authentication.setDetails(new
	// WebAuthenticationDetailsSource().buildDetails(request));
	// SecurityContextHolder.getContext().setAuthentication(authentication);
	// } else {
	// log.info("Validation fails !!");
	// }
	// } catch (UsernameNotFoundException e) {
	// log.error("User not found: {}", e.getMessage());
	// // Handle user not found scenario (e.g., send a specific response)
	// } catch (JwtException e) {
	// log.error("Token validation failed: {}", e.getMessage());
	// // Handle token validation failure (e.g., send a specific response)
	// } catch (Exception e) {
	// log.error("An unexpected error occurred: {}", e.getMessage());
	// // Handle any other exceptions
	// }
	//
	// }
	// filterChain.doFilter(request, response);
	// }

}
