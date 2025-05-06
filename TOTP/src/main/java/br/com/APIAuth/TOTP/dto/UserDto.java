package br.com.APIAuth.TOTP.dto;

import lombok.Data;

@Data
public class UserDto {
	
	//Atributos.
    private String email;
    private String secretKey;   
}

