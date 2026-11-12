package com.transfer.projeto.repository;

import com.transfer.projeto.model.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {

    boolean existsByOrigemAndDestinoAndValorAndCriadoEmGreaterThanEqual(
            String origem,
            String destino,
            Long valor,
            Instant since
    );
}
