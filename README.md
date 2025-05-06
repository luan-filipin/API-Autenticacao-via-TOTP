# API SpringBoot - Autenticação TOTP com QR Code
Sistema de geração de QR Code TOTP com Spring Boot, utilizando o padrão otpauth:// compatível com aplicativos como Microsoft Authenticator e Google Authenticator. Ideal para implementar autenticação em dois fatores (2FA).

## 🚀 Funcionalidades

- Geração de chave secreta TOTP única para cada email
- Criação de URL otpauth:// no padrão aceito por autenticadores
- Geração de imagem QR Code diretamente via backend
- Retorno do QR Code em formato PNG
- Estrutura organizada em camadas: Controller, Util, Model
- Salva os dados de email e secretKey do usuario no MongoDb.

## 🛠️ Tecnologias utilizadas

- Java 24
- Spring Boot
- Biblioteca Google Authenticator Java
- ZXing (para gerar o QR Code)
- Maven
- MongoDB (persistência de dados)

## 📡 Endpoint principal

Cadastrar usuario.
- `POST /api/auth/register`
```
{
  "email": "usuario@example.com",
  "password": "JBSWY3DPEHPK3PXP",
}
``` 


QR Code TOTP
- `POST /api/auth/qrcode`
```
{
  "email": "usuario@example.com",
  "password": "JBSWY3DPEHPK3PXP",
}
``` 
  Gera e retorna uma imagem PNG com o QR Code da chave secreta associada ao email informado.


Logar e testar o codigo OTP.
- `POST /api/auth/login`
```
{
  "email": "usuario@example.com",
  "password": "JBSWY3DPEHPK3PXP",
  "totpCode": "codigo OTP"
}
``` 

## 🗄️ Persistência de dados com MongoDB
A aplicação utiliza MongoDB como banco de dados para armazenar informações relacionadas à autenticação TOTP, como o e-mail do usuário e a chave secreta gerada.
A integração é feita com Spring Data MongoDB, permitindo salvar e consultar documentos de forma simples e eficiente.
```
{
  "id": "661fbdafc4e96c0712b4e123",
  "email": "usuario@example.com",
  "password": "$2a$10$LJBAnEsb4NeXuRSHTMw8remfHnCA9AA2iO9LT8KpifcTxHQC4sh2O"
  "secretKey": "JBSWY3DPEHPK3PXP",
  "verified": false
}
```
🔹 Exemplo de dado salvo:

## ✅ Exemplo de uso

Faça uma requisição para o endpoint informando o email via parâmetro email.
O QR Code retornado pode ser escaneado com apps como Microsoft Authenticator ou Google Authenticator.
O app irá começar a gerar códigos TOTP automaticamente.

## ⚠️ Observações

Este projeto ainda não implementa autenticação nem persistência em banco.
As informações geradas (email e chave secreta) não são armazenadas — isso será incluído nas próximas etapas.
A chave secreta é temporária e gerada a cada requisição para fins de teste.
