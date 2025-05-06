package br.com.APIAuth.TOTP.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.APIAuth.TOTP.dto.UserDto;
import br.com.APIAuth.TOTP.model.User;
import br.com.APIAuth.TOTP.repository.UserRepository;

@Service
public class UserServiceImp implements UserService {

	@Autowired
	private UserRepository userRepository; // Injeção de dependência do repositório de usuários.

	public User saveUser(UserDto userDto) {
		User user = new User();
		user.setEmail(userDto.getEmail()); // Define o email do usuário.
		user.setSecretKey(userDto.getSecretKey()); // Define a chave secreta do usuário.
		user.setVerified(false); // Define o status de verificação do usuário como falso.

		return userRepository.save(user); // Salva o usuário no banco de dados.
	}

}
