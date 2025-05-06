package br.com.APIAuth.TOTP.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.APIAuth.TOTP.model.User;

public interface UserRepository extends MongoRepository<User, String> {
	
	boolean existsByEmail(String email); //Método para encontrar o usuário pelo email.


}
