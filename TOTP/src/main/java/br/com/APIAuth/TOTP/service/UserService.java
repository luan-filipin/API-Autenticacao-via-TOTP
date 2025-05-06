package br.com.APIAuth.TOTP.service;

import java.util.Optional;

import br.com.APIAuth.TOTP.dto.UserDto;
import br.com.APIAuth.TOTP.model.User;

public interface UserService {
	
	User saveUser(UserDto userDto); //Método para salvar o usuário.

	Optional<User> findByEmail(String email);
	
	User updateUser(String email, UserDto userDto); //Método para atualizar o usuário.

}
