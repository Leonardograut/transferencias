package com.transfer.projeto.service;

import com.transfer.projeto.dto.AuthRequest;
import com.transfer.projeto.dto.LoginResponse;
import com.transfer.projeto.exception.ApiException;
import com.transfer.projeto.model.Usuario;
import com.transfer.projeto.repository.UsuarioRepository;
import com.transfer.projeto.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    public void registrar(AuthRequest request) {
        if (request == null || request.getUsername() == null || request.getUsername().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "DADOS_INVALIDOS");
        }

        String username = request.getUsername().trim();
        if (usuarioRepository.existsByUsername(username)) {
            throw new ApiException(HttpStatus.CONFLICT, "USUARIO_EXISTENTE");
        }

        Usuario usuario = new Usuario(
                username,
                passwordEncoder.encode(request.getPassword()),
                1000L
        );
        usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(AuthRequest request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "CREDENCIAIS_INVALIDAS");
        }

        String username = request.getUsername().trim();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "CREDENCIAIS_INVALIDAS"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "CREDENCIAIS_INVALIDAS");
        }

        String token = tokenService.gerarToken(usuario);
        return new LoginResponse(token);
    }
}
