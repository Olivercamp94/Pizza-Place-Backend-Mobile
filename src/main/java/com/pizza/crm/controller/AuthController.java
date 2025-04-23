package com.pizza.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.apache.commons.codec.binary.Base64;


import java.util.HashMap;
import java.util.Map;

import com.pizza.crm.service.ClientService;
import com.pizza.crm.utils.JwtUtil;
import com.pizza.crm.model.LoginRequest;
import com.pizza.crm.model.Client;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private ClientService clientService;  // Serviço que valida o login (detalhado abaixo)
    
    private final JwtUtil jwtUtil = new JwtUtil();

    // Endpoint de login para gerar tokens
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Validar as credenciais do usuário (exemplo simples)
        Client client = clientService.loginClient(loginRequest.getEmail(), loginRequest.getPassword());
        
        if (client != null) {
            // Gerar Access Token e Refresh Token
            String accessToken = jwtUtil.generateAccessToken(client.getEmail());
            String refreshToken = jwtUtil.generateRefreshToken(client.getEmail());

            // Retornar os tokens
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            return ResponseEntity.ok(tokens);
        }

        return ResponseEntity.status(401).body("Invalid credentials");
    }

    // Endpoint de refresh para gerar um novo Access Token com o Refresh Token
    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader("Authorization") String authorizationHeader) {
        
        return ResponseEntity.status(200).body("RT: " + authorizationHeader);
        // try {
        //     // Validar o refreshToken
        //     String email = jwtUtil.extractUsername(refreshToken); // Método que extrai o email do refresh token

        //     // Verificar se o refresh token expirou
        //     if (email != null && jwtUtil.isTokenExpired(refreshToken)) {
        //         return ResponseEntity.status(401).body("Refresh Token Expired");
        //     }

        //     // Gerar um novo access token com o e-mail extraído
        //     String newAccessToken = jwtUtil.generateAccessToken(email);

        //     // Retornar o novo access token
        //     return ResponseEntity.ok(newAccessToken);
        // } catch (Exception e) {
        //     return ResponseEntity.status(400).body("Invalid Refresh Token: " + e + "\n" + refreshToken);
        // }
    }
}
