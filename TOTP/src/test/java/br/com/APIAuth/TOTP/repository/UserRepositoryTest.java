package br.com.APIAuth.TOTP.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import br.com.APIAuth.TOTP.model.User;

@DataMongoTest
public class UserRepositoryTest {
	
	@Mock
	private UserRepository userRepository; // Injeção de dependência do repositório de usuários.
	
	private User user;
	
	@BeforeEach
	void setUp() {
		
		user = new User();
		user.setEmail("teste@email.com");
		user.setPassword("123456");
		user.setSecretKey("secretKey");
		user.setVerified(false);	
	}
	
	@Test
	void testExistsByEmail() {
		//Simula que o email existe no banco.
		when(userRepository.existsByEmail("teste@email.com")).thenReturn(true);
		
		//Verifica se o metodo retorna o valor esperado.
		boolean exists = userRepository.existsByEmail("teste@email.com");
		
		//Verifica a asserção.
		assertTrue(exists);
	}
	
	@Test
	void testFindByEmail() {
		//Simula a busca do usuario pelo email.
		when(userRepository.findByEmail("teste@email.com")).thenReturn(java.util.Optional.of(user));
		
		//Busca o usuario pelo email.
		java.util.Optional<User> foundUser = userRepository.findByEmail("teste@email.com");
		
		//Verifica a asserção.
		assertTrue(foundUser.isPresent());
		assertEquals("teste@email.com", foundUser.get().getEmail());
	}
	
	@Test
	void testFindByEmailNotFound() {
		
		//Simula que nao existe usuario com esse email.
		when(userRepository.findByEmail("notfound@example.com")).thenReturn(java.util.Optional.empty());
		
		//Busca usuario que nao existe.
		java.util.Optional<User> foundUser = userRepository.findByEmail("notfound@example.com");
		
		//Verifica que o usuario nao foi encontrado.
		assertFalse(foundUser.isPresent());
		
	}
	
}
