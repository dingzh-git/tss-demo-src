package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.httpBasic(this::configHttpBasicWhenSecurityDisabled);
		http.authorizeHttpRequests(this::configAutoHttpReqWhenSecurityDisabled);
		
		http.csrf(this::configCsrf);
		http.exceptionHandling(this::configExceptionHandling);
		http.sessionManagement(this::configSessionManagement);
		return http.build();
	}


    @Bean
    AuthenticationEntryPoint unauthorizedEntryPoint() {
		
		return (request, response, authException) -> {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());

			response.addHeader("WWW-Authenticate", "Basic realm=\"Realm\"");
			
			String responseBody = "{error: {code=\"xxxxx\", message=\"XXXXXX\"}";
			
			response.getOutputStream().println(responseBody);
			
			log.info("UnauthorizedError: " + responseBody + ".");
			
		};
	}
	
	private void configHttpBasicWhenSecurityDisabled(HttpBasicConfigurer<HttpSecurity> httpBasicConfig) {
		httpBasicConfig.disable();
	}
	
	private void configAutoHttpReqWhenSecurityDisabled(
			AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry matherRegistry) {
		matherRegistry.anyRequest().permitAll();
	}
	
	private void configCsrf(CsrfConfigurer<HttpSecurity> csrfConfig) {
		csrfConfig.disable();
	}
	
	private void configExceptionHandling(ExceptionHandlingConfigurer<HttpSecurity> exceptionHandlingConfig) {
		exceptionHandlingConfig.authenticationEntryPoint(unauthorizedEntryPoint());
	}
	
	private void configSessionManagement(SessionManagementConfigurer<HttpSecurity> sessionManagementConfig) {
		sessionManagementConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
}