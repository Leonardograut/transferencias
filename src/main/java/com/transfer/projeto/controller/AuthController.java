package com.transfer.projeto.controller;

import com.transfer.projeto.dto.AuthRequest;
import com.transfer.projeto.dto.LoginResponse;
import com.transfer.projeto.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registrar")
    public ResponseEntity<Void> registrar(@RequestBody(required = false) AuthRequest request) {
        authService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody(required = false) AuthRequest request) {
        return ResponseEntity.ok().body(authService.login(request));
    }
}
