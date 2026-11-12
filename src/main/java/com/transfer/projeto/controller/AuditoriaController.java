package com.transfer.projeto.controller;

import com.transfer.projeto.dto.AuditoriaResponse;
import com.transfer.projeto.security.SecurityConfig;
import com.transfer.projeto.service.AuditoriaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auditoria")
@RequiredArgsConstructor
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @GetMapping
    public ResponseEntity<AuditoriaResponse> getAuditoria() {
        return ResponseEntity.ok().body(auditoriaService.getAuditoria());
    }
}
