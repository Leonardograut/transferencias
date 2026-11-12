package com.transfer.projeto.controller;

import com.transfer.projeto.dto.TransferenciaRequest;
import com.transfer.projeto.dto.TransferenciaResponse;
import com.transfer.projeto.exception.ApiException;
import com.transfer.projeto.security.SecurityConfig;
import com.transfer.projeto.service.TransferenciaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transferencias")
@RequiredArgsConstructor
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class TransferenciaController {

    private final TransferenciaService transferenciaService;

    @PostMapping
    public ResponseEntity<TransferenciaResponse> transferir(
            @RequestBody(required = false) TransferenciaRequest dados,
            HttpServletRequest request
    ) {

        String username = (String) request.getAttribute("authenticatedUsername");

        if (username == null) {
            Authentication auth = SecurityContextHolder
                    .getContext()
                    .getAuthentication();

            if (auth != null && auth.isAuthenticated()) {
                username = auth.getName();
            }
        }

        if (username == null) {
            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    "NAO_AUTENTICADO"
            );
        }

        TransferenciaResponse response =
                transferenciaService.transferir(username,dados);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
