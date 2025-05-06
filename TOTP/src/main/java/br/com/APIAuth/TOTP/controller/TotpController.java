package br.com.APIAuth.TOTP.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import br.com.APIAuth.TOTP.dto.LoginDto;
import br.com.APIAuth.TOTP.dto.UserDto;
import br.com.APIAuth.TOTP.model.User;
import br.com.APIAuth.TOTP.repository.UserRepository;
import br.com.APIAuth.TOTP.service.UserService;
import br.com.APIAuth.TOTP.service.UserServiceImp;
import br.com.APIAuth.TOTP.util.QRCodeGenerator;
import br.com.APIAuth.TOTP.util.TotpUtil;

@RestController
@RequestMapping("/api/auth")
public class TotpController {

	@Autowired
	private UserService userService; // Injeção de dependência do serviço de usuários.
	@Autowired
	private PasswordEncoder passwordEncoder; // Injeção de dependência do codificador de senhas.

	@PostMapping(value = "/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> getQrCode(@RequestBody LoginDto loginDto) {
	    try {
	        // Verifica se o usuário existe
	        Optional<User> userOptional = userService.findByEmail(loginDto.getEmail());
	        if (userOptional.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	        }

	        User user = userOptional.get();

	        // Verifica se a senha está correta
	        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	        }

	        // Geração da chave secreta
	        GoogleAuthenticatorKey key = TotpUtil.generateSecretKey();

	        // Gerar URL e imagem do QR Code
	        String otpAuthURL = TotpUtil.getQRBarcode(loginDto.getEmail(), key);
	        byte[] qrImage = QRCodeGenerator.generateQRCodeImage(otpAuthURL, 200, 200);

	        // Atualizar a chave secreta do usuário no banco de dados
	        UserDto userDto = new UserDto();
	        userDto.setSecretKey(key.getKey());
	        userService.updateUser(user.getEmail(), userDto);

	        // Retornar a imagem do QR Code
	        return ResponseEntity.ok(qrImage);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}


	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody UserDto userDto) {
	    GoogleAuthenticatorKey key = TotpUtil.generateSecretKey();
	    userDto.setSecretKey(key.getKey());

	    User savedUser = userService.saveUser(userDto);
	    return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
	    try {
	        // Verifica se o usuário existe
	        Optional<User> userOptional = userService.findByEmail(loginDto.getEmail());

	        if (userOptional.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não encontrado");
	        }

	        User user = userOptional.get();

	        // Verifica se a senha está correta
	        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha inválida.");
	        }

	        // Verifica código TOTP
	        boolean isCodeValid = TotpUtil.verifyCode(user.getSecretKey(), loginDto.getTotpCode());

	        if (!isCodeValid) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Código TOTP inválido.");
	        }

	        return ResponseEntity.ok("Login bem-sucedido");

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}


}
