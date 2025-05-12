package br.com.APIAuth.TOTP.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.APIAuth.TOTP.dto.UserDto;
import br.com.APIAuth.TOTP.exception.EmailAlreadyExistsException;
import br.com.APIAuth.TOTP.mapper.UserMapper;
import br.com.APIAuth.TOTP.model.User;
import br.com.APIAuth.TOTP.repository.UserRepository;

@Service
public class UserServiceImp implements UserService {

	private final UserRepository userRepository; 
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;
	
   
    public UserServiceImp(PasswordEncoder passwordEncoder, UserRepository userRepository, UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    
	public User saveUser(UserDto userDto) {
		
	    if (userRepository.existsByEmail(userDto.getEmail())) {
	        throw new EmailAlreadyExistsException("Usuário com esse e-mail já está cadastrado.");
	    }
		
	    User user = userMapper.toEntity(userDto); // Mapeia o DTO para a entidade User.
	    user.setEmail(userDto.getEmail()); // Define o email do usuário.
	    user.setSecretKey(userDto.getSecretKey()); // Define a chave secreta do usuário.
	    user.setPassword(passwordEncoder.encode(userDto.getPassword())); // Codifica a senha.
	    user.setVerified(false); // Define o status de verificação do usuário como falso.
		
	    return userRepository.save(user); // Salva o usuário no banco de dados.
	}

	@Override
	public Optional<User> findByEmail(String email) {
	    return userRepository.findByEmail(email);
	}
	
	
    public User updateUser(String email, UserDto userDto) {
        // Verifica se o usuário existe
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado com o email: " + email);
        }

        // Obtém o usuário
        User user = userOptional.get();

        // Atualiza os dados do usuário
        user.setPassword(userDto.getPassword() != null ? passwordEncoder.encode(userDto.getPassword()) : user.getPassword());
        user.setSecretKey(userDto.getSecretKey() != null ? userDto.getSecretKey() : user.getSecretKey());
        // Caso tenha outros campos a atualizar, pode fazer da mesma forma

        // Salva o usuário atualizado
        return userRepository.save(user);
    }


}
