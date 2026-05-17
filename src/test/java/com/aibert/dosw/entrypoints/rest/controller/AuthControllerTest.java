package com.aibert.dosw.entrypoints.rest.controller;

import com.aibert.dosw.application.dto.request.LoginRequestDTO;
import com.aibert.dosw.application.dto.response.LoginResponseDTO;
import com.aibert.dosw.application.service.TokenBlacklistService;
import com.aibert.dosw.domain.ports.in.LoginUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock private LoginUseCase loginUseCase;
    @Mock private TokenBlacklistService tokenBlacklistService;
    @InjectMocks private AuthController authController;

    @Test
    void login_exitoso_retorna200() {
        LoginResponseDTO response = LoginResponseDTO.builder()
                .id(UUID.randomUUID()).fullName("Test").email("test@mail.escuelaing.edu.co")
                .role("ESTUDIANTE").token("jwt").profileComplete(true).build();
        when(loginUseCase.login(any())).thenReturn(response);

        ResponseEntity<LoginResponseDTO> result = authController.login(mock(LoginRequestDTO.class));

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("jwt", result.getBody().getToken());
    }

    @Test
    void logout_conBearer_invalidaToken() {
        ResponseEntity<Map<String, Object>> result = authController.logout("Bearer my-token");

        verify(tokenBlacklistService).invalidate("my-token");
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue((Boolean) result.getBody().get("sessionClosed"));
        assertTrue((Boolean) result.getBody().get("redirectLogin"));
    }

    @Test
    void logout_sinHeader_noInvalida() {
        ResponseEntity<Map<String, Object>> result = authController.logout(null);
        verify(tokenBlacklistService, never()).invalidate(any());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void logout_headerSinBearer_noInvalida() {
        ResponseEntity<Map<String, Object>> result = authController.logout("Basic abc123");
        verify(tokenBlacklistService, never()).invalidate(any());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
