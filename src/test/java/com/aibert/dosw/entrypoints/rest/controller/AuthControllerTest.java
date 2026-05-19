package com.aibert.dosw.entrypoints.rest.controller;

import com.aibert.dosw.application.dto.request.LoginRequestDTO;
import com.aibert.dosw.application.dto.response.LoginResponseDTO;
import com.aibert.dosw.application.dto.response.PasswordResetResponseDTO;
import com.aibert.dosw.application.service.TokenBlacklistService;
import com.aibert.dosw.domain.ports.in.LoginUseCase;
import com.aibert.dosw.domain.ports.in.PasswordResetUseCase;
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
    @Mock private PasswordResetUseCase passwordResetUseCase;
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

    @Test
    void forgotPassword_exitoso_retorna200() {
        PasswordResetResponseDTO response = PasswordResetResponseDTO.builder()
                .recoveryStatus(true).expirationTime(5).build();
        when(passwordResetUseCase.requestPasswordReset(any())).thenReturn(response);

        ResponseEntity<PasswordResetResponseDTO> result = authController.forgotPassword("test@mail.escuelaing.edu.co");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isRecoveryStatus());
    }

    @Test
    void resetPassword_exitoso_retornaMensaje() {
        doNothing().when(passwordResetUseCase).resetPassword(any(), any(), any());
        ResponseEntity<String> result = authController.resetPassword("token", "NewPass1", "NewPass1");
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }
}
