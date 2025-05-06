package br.com.APIAuth.TOTP.dto;

import lombok.Data;

@Data
public class LoginDto {
	
	private String email;
	private String password;
	private int totpCode;
}
