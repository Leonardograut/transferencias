package com.transfer.projeto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuditoriaResponse {
    private Long usuarios;
    private Long somaSaldos;
    private Long transferencias;
}
