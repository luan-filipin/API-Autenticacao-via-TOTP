package br.com.APIAuth.TOTP.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

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
	private UserService userService; //Injeção de dependência do serviço de usuários.

    @GetMapping(value = "/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQrCode(@RequestParam String email) {
        try {
        	// Geração da chave secreta
            GoogleAuthenticatorKey key = TotpUtil.generateSecretKey();

            // Gerar URL e imagem do QR Code
            String otpAuthURL = TotpUtil.getQRBarcode(email, key);
            byte[] qrImage = QRCodeGenerator.generateQRCodeImage(otpAuthURL, 200, 200);

            //Salvar o usuário no banco de dados
            UserDto userDto = new UserDto();
            userDto.setEmail(email);
            userDto.setSecretKey(key.getKey());
            userService.saveUser(userDto);
            
            // Retornar a imagem do QR Code
            return ResponseEntity.ok(qrImage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

