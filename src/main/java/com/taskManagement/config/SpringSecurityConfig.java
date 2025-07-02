package com.taskManagement.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.taskManagement.jwtEntryPoint.JwtAuthnticationEntryPoint;
import com.taskManagement.jwtFilter.JwtFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SpringSecurityConfig {

	private static final String[] AUTH_WHITELIST = { "/**", "/user/**", "swagger-ui.html", "/swagger-ui/**",
			"/v3/api-docs/**", "/swagger-resources/**", "/webjars/**", "/api-docs/**", "gourav/**" };

	private JwtFilter filter;
	private JwtAuthnticationEntryPoint entryPoint;

	public SpringSecurityConfig(JwtFilter filter, JwtAuthnticationEntryPoint entryPoint) {
		super();
		this.filter = filter;
		this.entryPoint = entryPoint;
	}
	//
	// @Bean
	// SecurityFilterChain customSecurityFilterChain(HttpSecurity http) throws
	// Exception {
	//
	// http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configure(http))
	// .authorizeHttpRequests(
	// auth ->
	// auth.requestMatchers(AUTH_WHITELIST).permitAll().anyRequest().authenticated())
	// .exceptionHandling(ex ->
	// ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
	// .sessionManagement(session ->
	// session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	//
	// http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
	//
	// return http.build();
	// }

	// @Bean
	// SecurityFilterChain customSecurityFilterChain(HttpSecurity http) throws
	// Exception {
	// http.csrf(csrf -> csrf.disable()).cors(cors ->
	// cors.configurationSource(corsConfigurationSource()))
	// .authorizeHttpRequests(
	// auth ->
	// auth.requestMatchers(AUTH_WHITELIST).permitAll().anyRequest().authenticated())
	// .exceptionHandling(ex ->
	// ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
	// .sessionManagement(session ->
	// session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	//
	// http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
	// return http.build();
	// }
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
		return builder.getAuthenticationManager();
	}

	// @Bean
	// UrlBasedCorsConfigurationSource corsConfigurationSource() {
	// CorsConfiguration corsConfiguration = new CorsConfiguration();
	// // Add your allowed origins
	// corsConfiguration.addAllowedOrigin("http://localhost:3000/");
	// //
	// corsConfiguration.addAllowedOrigin("https://app.ventureconsultancyservices.com");
	// corsConfiguration.addAllowedHeader("*");
	// corsConfiguration.addAllowedMethod("*");
	// corsConfiguration.setAllowCredentials(true);
	//
	// UrlBasedCorsConfigurationSource source = new
	// UrlBasedCorsConfigurationSource();
	// source.registerCorsConfiguration("/*", corsConfiguration);
	// return source;
	// }

	// @Bean
	// CorsFilter corsFilter() {
	//
	// CorsConfiguration corsConfiguration = new CorsConfiguration();
	// corsConfiguration.addAllowedOrigin("http://localhost:3000");
	// corsConfiguration.addAllowedHeader("*");
	// corsConfiguration.addAllowedMethod("*");
	// corsConfiguration.setAllowCredentials(true);
	// UrlBasedCorsConfigurationSource source = new
	// UrlBasedCorsConfigurationSource();
	// source.registerCorsConfiguration("/**", corsConfiguration);
	// return new CorsFilter(source);
	// }

//	@Bean
//	public CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000/"));
//		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
//		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
//		configuration.setAllowCredentials(true);
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", configuration);
//		return source;
//	}

	@Bean
	SecurityFilterChain customSecurityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.authorizeHttpRequests(auth ->
				/*
				 * .requestMatchers("/auth/**") .permitAll().requestMatchers("/**",
				 * "/api/getdata").hasRole("USER")
				 */
				auth.requestMatchers(AUTH_WHITELIST).permitAll().anyRequest().authenticated())
				.exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(List.of("*"));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
		configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
