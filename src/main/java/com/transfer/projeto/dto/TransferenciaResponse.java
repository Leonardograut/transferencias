package com.transfer.projeto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransferenciaResponse {
    private Long id;
    private String origem;
    private String destino;
    private Long valor;
    private Long saldo;
}
