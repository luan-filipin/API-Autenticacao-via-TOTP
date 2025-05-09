package br.com.APIAuth.TOTP.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.APIAuth.TOTP.dto.UserDto;
import br.com.APIAuth.TOTP.model.User;
import br.com.APIAuth.TOTP.repository.UserRepository;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class UserServiceTest {
	
	@Mock
	private UserRepository userRepository; // Injeção de dependência do repositório de usuários.
	
	@Mock
	private PasswordEncoder passwordEncoder; // Injeção de dependência do PasswordEncoder.
	
	@InjectMocks
	private UserServiceImp userService; // Injeção de dependência do serviço de usuários.
	
	@Captor
	ArgumentCaptor<User> userCaptor;

	@Test
	void shouldSaveUserWhenEmailDoesNotExist() {
		//Alimenta o Dto com dados de teste.
	    UserDto userDto = new UserDto("teste@email.com", "123456", "chaveSecreta");

	    // Configuração do mock
	    when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
	    when(passwordEncoder.encode(userDto.getPassword())).thenReturn("senhaCodificada");
	    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

	    // Chamada do método a ser testado
	    User savedUser = userService.saveUser(userDto);

	    // Captura o usuário salvo
	    verify(userRepository).save(userCaptor.capture());
	    // Verifica se o método encode foi chamado com a senha original
	    verify(passwordEncoder).encode(userDto.getPassword());
	    User capturedUser = userCaptor.getValue();


	    // Asserções
	    assertEquals(userDto.getEmail(), capturedUser.getEmail());
	    assertEquals("senhaCodificada", capturedUser.getPassword()); // Verifica a senha criptografada
	    assertEquals(userDto.getSecretKey(), capturedUser.getSecretKey());
	    assertFalse(capturedUser.isVerified());
	}

	
	@Test
	void shouldThrowExceptionWhenEmailAlreadyExists() {
		//Alimenta o Dto com dados de teste.
		UserDto userDto = new UserDto("email@exemplo.com", "senha123", "chaveSecreta");
		
		when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true); // Simula que o email já existe no banco.
		
		assertThrows(br.com.APIAuth.TOTP.exception.EmailAlreadyExistsException.class, () -> {
			userService.saveUser(userDto); // Chama o método a ser testado.
		}); // Verifica se a exceção é lançada.
		
		verify(userRepository).existsByEmail(userDto.getEmail()); // Verifica se o método existsByEmail foi chamado com o email esperado.
		
	}
	
	@Test
	void deveAtualizarDadosQuandoEmailExiste() {
		//Alimenta o Dto com dados de teste.
		UserDto userDto = new UserDto("teste@email.com", "123456", "chaveSercreta");
		
		when(userRepository.findByEmail(userDto.getEmail())).thenReturn(java.util.Optional.of(new User())); // Simula que o email existe no banco.
		when(passwordEncoder.encode(userDto.getPassword())).thenReturn("senhaCodificada");
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Simula o salvamento do usuário.
		
		User updatedUser = userService.updateUser(userDto.getEmail(), userDto); // Chama o método a ser testado.
		
        verify(userRepository).save(userCaptor.capture()); // Captura o usuário salvo.
        verify(passwordEncoder).encode(userDto.getPassword()); // Verifica se o método encode foi chamado com a senha original.
        User capturedUser = userCaptor.getValue(); // Obtém o usuário capturado.
        
        // Asserções
        assertEquals("senhaCodificada", capturedUser.getPassword());// Verifica a senha criptografada.
        assertEquals(userDto.getSecretKey(), capturedUser.getSecretKey()); // Verifica a chave secreta.
	}
	
	@Test
	void deveLancarExcecaoQuandoEmailNaoExiste() {
		//Alimenta o Dto com dados de teste.
		UserDto userDto = new UserDto("teste@email.com", "123456", "chaveSecreta");
		
		// Simula que o email não existe no banco.
		when(userRepository.findByEmail(userDto.getEmail())).thenReturn(java.util.Optional.empty());
		
	    // Verifica se a exceção é lançada
	    assertThrows(UsernameNotFoundException.class, () -> {
	        userService.updateUser(userDto.getEmail(), userDto);
	    });
	    
	    // Garante que o save não foi chamado
	    verify(userRepository).findByEmail(userDto.getEmail());
	}
	
	
	

}
