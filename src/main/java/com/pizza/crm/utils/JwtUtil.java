package com.pizza.crm.utils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import java.util.Date;
import io.github.cdimascio.dotenv.Dotenv;


public class JwtUtil {

    Dotenv dotenv = Dotenv.load();
    private final String SECRET_KEY = dotenv.get("JWT_SECRET");  

    public String generateAccessToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))  // 1 hora de expiração
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();
    }

    // Gera um Refresh Token com 30 dias de validade
    public String generateRefreshToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 30))  // 30 dias de expiração
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();
    }

    // Valida o token e extrai as informações
    public Claims extractClaims(String token) {
        return Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .getBody();
    }

    // Verifica se o token está expirado
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Obtém o nome de usuário do token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Verifica se o token é válido
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }
}
