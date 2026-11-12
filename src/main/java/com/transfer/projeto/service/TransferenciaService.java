package com.transfer.projeto.service;

import com.transfer.projeto.dto.TransferenciaRequest;
import com.transfer.projeto.dto.TransferenciaResponse;
import com.transfer.projeto.exception.ApiException;
import com.transfer.projeto.model.Transferencia;
import com.transfer.projeto.model.Usuario;
import com.transfer.projeto.repository.TransferenciaRepository;
import com.transfer.projeto.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class TransferenciaService {

    private final UsuarioRepository usuarioRepository;
    private final TransferenciaRepository transferenciaRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public TransferenciaResponse transferir(
            String origemUsername,
            TransferenciaRequest dados) {

        if (dados == null) {
            throw new ApiException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "VALOR_INVALIDO"
            );
        }

        if (dados.getValor() == null ||  dados.getValor() <= 0) {
            throw new ApiException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "VALOR_INVALIDO"
            );
        }

        long valor = dados.getValor();

        if (dados.getDestino() == null || dados.getDestino().isBlank()) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "DESTINO_INEXISTENTE"
            );
        }

        String destinoUsername = dados.getDestino().trim();

        if (origemUsername.equals(destinoUsername)) {
            throw new ApiException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "MESMA_CONTA"
            );
        }

        if (!usuarioRepository.existsByUsername(destinoUsername)) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "DESTINO_INEXISTENTE"
            );
        }

        //  Bloqueio pessimista em ordem alfabética
        // Evita deadlocks entre transferências simultâneas.

        String firstLock = origemUsername.compareTo(destinoUsername) < 0
                ? origemUsername : destinoUsername;

        String secondLock = origemUsername.compareTo(destinoUsername) < 0
                ? destinoUsername : origemUsername;

        Usuario u1 = usuarioRepository.findByUsernameWithLock(firstLock)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "DESTINO_INEXISTENTE"
                ));

        Usuario u2 = usuarioRepository.findByUsernameWithLock(secondLock)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "DESTINO_INEXISTENTE"
                ));

        Usuario origem = u1.getUsername().equals(origemUsername) ? u1 : u2;

        Usuario destino = u1.getUsername().equals(destinoUsername) ? u1 : u2;

        //  Verificar duplicidade nos últimos 60 segundos
        Instant cutoff = Instant.now().minus(60, ChronoUnit.SECONDS);

        boolean duplicate =
                transferenciaRepository
                        .existsByOrigemAndDestinoAndValorAndCriadoEmGreaterThanEqual(
                                origemUsername,
                                destinoUsername,
                                valor,
                                cutoff
                        );

        if (duplicate) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "TRANSFERENCIA_DUPLICADA"
            );
        }

        if (origem.getSaldo() < valor) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "SALDO_INSUFICIENTE"
            );
        }

        origem.setSaldo(origem.getSaldo() - valor);
        destino.setSaldo(destino.getSaldo() + valor);

        usuarioRepository.save(origem);
        usuarioRepository.save(destino);

        Transferencia transferencia = new Transferencia(
                origemUsername,
                destinoUsername,
                valor,
                Instant.now()
        );

        transferencia = transferenciaRepository.save(transferencia);

        return new TransferenciaResponse(
                transferencia.getId(),
                origemUsername,
                destinoUsername,
                valor,
                origem.getSaldo()
        );
    }
}
