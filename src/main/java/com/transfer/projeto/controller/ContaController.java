package com.transfer.projeto.controller;

import com.transfer.projeto.dto.ContaResponse;
import com.transfer.projeto.security.SecurityConfig;
import com.transfer.projeto.service.ContaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conta")
@RequiredArgsConstructor
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class ContaController {

    private final ContaService contaService;

    @GetMapping
    public ResponseEntity<ContaResponse> getConta(HttpServletRequest request) {
        String username = (String) request.getAttribute("authenticatedUsername");
        if (username == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                username = auth.getName();
            }
        }
        return ResponseEntity.ok(contaService.getConta(username));
    }
}
