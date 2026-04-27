package com.grootology.auth.entrypoints.rest.controller;

import com.grootology.auth.application.dto.request.LoginRequestDTO;
import com.grootology.auth.application.dto.response.LoginResponseDTO;
import com.grootology.auth.domain.ports.in.LoginUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginUseCase loginUseCase;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(loginUseCase.login(request));
    }
}
