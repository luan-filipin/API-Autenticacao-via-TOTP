API SpringBoot - Autenticação TOTP com QR Code
Sistema de geração de QR Code TOTP com Spring Boot, utilizando o padrão otpauth:// compatível com aplicativos como Microsoft Authenticator e Google Authenticator. Ideal para implementar autenticação em dois fatores (2FA).

🚀 Funcionalidades
Geração de chave secreta TOTP única para cada email

Criação de URL otpauth:// no padrão aceito por autenticadores

Geração de imagem QR Code diretamente via backend

Retorno do QR Code em formato PNG

Estrutura organizada em camadas: Controller, Util, Model

🛠️ Tecnologias utilizadas
Java 24

Spring Boot

Biblioteca Google Authenticator Java

ZXing (para gerar o QR Code)

Maven

📡 Endpoint principal
QR Code TOTP
GET /api/auth/qrcode?email=seuemail@exemplo.com
Gera e retorna uma imagem PNG com o QR Code da chave secreta associada ao email informado.

✅ Exemplo de uso
Faça uma requisição para o endpoint informando o email via parâmetro email.

O QR Code retornado pode ser escaneado com apps como Microsoft Authenticator ou Google Authenticator.

O app irá começar a gerar códigos TOTP automaticamente.

⚠️ Observações
Este projeto ainda não implementa autenticação nem persistência em banco.

As informações geradas (email e chave secreta) não são armazenadas — isso será incluído nas próximas etapas.

A chave secreta é temporária e gerada a cada requisição para fins de teste.
