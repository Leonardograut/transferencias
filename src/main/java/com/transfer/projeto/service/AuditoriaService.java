package com.transfer.projeto.service;

import com.transfer.projeto.dto.AuditoriaResponse;
import com.transfer.projeto.repository.TransferenciaRepository;
import com.transfer.projeto.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final UsuarioRepository usuarioRepository;
    private final TransferenciaRepository transferenciaRepository;

    @Transactional(readOnly = true)
    public AuditoriaResponse getAuditoria() {
        long usuarios = usuarioRepository.count();
        Long somaSaldos = usuarioRepository.sumSaldos();
        long transferencias = transferenciaRepository.count();

        return new AuditoriaResponse(
                usuarios,
                somaSaldos != null ? somaSaldos : 0L,
                transferencias
        );
    }
}
