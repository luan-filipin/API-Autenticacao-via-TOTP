package br.com.APIAuth.TOTP.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
	
	//Atributos.
    private String email;
    private String password;
    private String secretKey;   
}

