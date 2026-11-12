package com.transfer.projeto.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.transfer.projeto.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class TokenService {

    @Value("${api.security.token.secret:minha-chave-secreta-muito-segura-e-longa-para-jwt-transfer-api-2026!}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        return JWT.create()
                .withIssuer("api-jwt")
                .withSubject(usuario.getUsername())
                .withClaim("id", usuario.getId())
                .withExpiresAt(LocalDateTime.now()
                        .plusHours(24).toInstant(ZoneOffset.of("-03:00")))
                .sign(Algorithm.HMAC256(secret));
    }

    public Optional<String> validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return Optional.ofNullable(JWT.require(algorithm)
                    .withIssuer("api-jwt")
                    .build()
                    .verify(token)
                    .getSubject());
        } catch (JWTVerificationException e) {
            return Optional.empty();
        }
    }
}
