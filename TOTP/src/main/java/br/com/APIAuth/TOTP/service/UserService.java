package br.com.APIAuth.TOTP.service;

import br.com.APIAuth.TOTP.dto.UserDto;
import br.com.APIAuth.TOTP.model.User;

public interface UserService {
	
	User saveUser(UserDto userDto); //Método para salvar o usuário.

}
