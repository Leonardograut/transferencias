package com.transfer.projeto.service;

import com.transfer.projeto.dto.ContaResponse;
import com.transfer.projeto.exception.ApiException;
import com.transfer.projeto.model.Usuario;
import com.transfer.projeto.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContaService {

    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public ContaResponse getConta(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "NAO_AUTENTICADO"));

        return new ContaResponse(usuario.getUsername(), usuario.getSaldo());
    }
}
