package com.transfer.projeto.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@Entity
@Table(name = "transferencias", indexes = {
    @Index(name = "idx_dup_check", columnList = "origem, destino, valor, criadoEm")
})
@Getter
@Setter
@NoArgsConstructor
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String origem;

    @Column(nullable = false, length = 100)
    private String destino;

    @Column(nullable = false)
    private Long valor;

    @Column(nullable = false)
    private Instant criadoEm;

    public Transferencia(String origem, String destino, Long valor, Instant criadoEm) {
        this.origem = origem;
        this.destino = destino;
        this.valor = valor;
        this.criadoEm = criadoEm;
    }
}
