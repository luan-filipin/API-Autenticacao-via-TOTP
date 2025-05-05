package br.com.APIAuth.TOTP.util;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

public class TotpUtil {
    
    private static final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    public static GoogleAuthenticatorKey generateSecretKey() {
        return gAuth.createCredentials();
    }

    public static String getQRBarcode(String email, GoogleAuthenticatorKey secret) {
        // Corrige o label e inclui issuer no formato correto para compatibilidade
        String issuer = "SecureAuthApp";
        String label = issuer + ":" + email;

        return String.format(
            "otpauth://totp/%s?secret=%s&issuer=%s",
            label,
            secret.getKey(),
            issuer
        );
    }

    public static boolean verifyCode(String secret, int code) {
        return gAuth.authorize(secret, code);
    }
}

