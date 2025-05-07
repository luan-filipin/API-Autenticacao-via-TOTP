package br.com.APIAuth.TOTP.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers(
	                "/api/auth/register", // Endpoint para registro de usuários.
	                "/api/auth/login", // Endpoint para testar a autenticação.
	                "/api/auth/qrcode", // Endpoint para gerar o QR Code.
	                "/v3/api-docs/**", // Documentação da API.
	                "/v3/api-docs", // Documentação da API.
	                "/swagger-ui/**", // Interface do Swagger.
	                "/swagger-ui.html", // Interface do Swagger.
	                "/swagger-resources/**", // Recursos do Swagger.
	                "/webjars/**" // Recursos do Swagger.
	            ).permitAll()
	            .anyRequest().authenticated()
	        );

	    return http.build();
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
