package com.aibert.dosw.application.service;

import com.aibert.dosw.application.dto.response.PasswordResetResponseDTO;
import com.aibert.dosw.domain.exceptions.InvalidCredentialsException;
import com.aibert.dosw.domain.model.user.PasswordResetToken;
import com.aibert.dosw.domain.model.user.Role;
import com.aibert.dosw.domain.model.user.User;
import com.aibert.dosw.domain.ports.out.EmailServicePort;
import com.aibert.dosw.domain.ports.out.PasswordResetTokenPort;
import com.aibert.dosw.domain.ports.out.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceTest {

    @Mock private UserRepositoryPort userRepository;
    @Mock private PasswordResetTokenPort tokenPort;
    @Mock private EmailServicePort emailService;
    @Mock private BCryptPasswordEncoder bcryptValidator;
    @InjectMocks private PasswordResetService passwordResetService;

    private User buildUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .fullName("Test User")
                .email("test@mail.escuelaing.edu.co")
                .password("hashedPassword")
                .verified(true)
                .role(Role.ESTUDIANTE)
                .status(com.aibert.dosw.domain.model.user.UserStatus.ACTIVO)
                .failedAttempts(0)
                .build();
    }

    private PasswordResetToken buildToken(boolean used, LocalDateTime expiresAt) {
        return PasswordResetToken.builder()
                .id(1L)
                .token("valid-token")
                .userId(UUID.randomUUID())
                .expiresAt(expiresAt)
                .used(used)
                .build();
    }

    @Test
    void requestPasswordReset_emailExiste_enviaCorreoYRetornaDTO() {
        ReflectionTestUtils.setField(passwordResetService, "baseUrl", "http://localhost:8080");
        User user = buildUser();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(tokenPort.save(any())).thenReturn(buildToken(false, LocalDateTime.now().plusMinutes(5)));

        PasswordResetResponseDTO result = passwordResetService.requestPasswordReset("test@mail.escuelaing.edu.co");

        assertTrue(result.isRecoveryStatus());
        assertEquals(5, result.getExpirationTime());
        verify(emailService).sendRecoveryEmail(any(), any());
    }

    @Test
    void requestPasswordReset_emailNoExiste_retornaDTOSinEnviarCorreo() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        PasswordResetResponseDTO result = passwordResetService.requestPasswordReset("noexiste@mail.escuelaing.edu.co");

        assertTrue(result.isRecoveryStatus());
        verify(emailService, never()).sendRecoveryEmail(any(), any());
    }

    @Test
    void resetPassword_tokenValido_actualizaContrasena() {
        UUID userId = UUID.randomUUID();
        PasswordResetToken token = PasswordResetToken.builder()
                .id(1L).token("valid-token").userId(userId)
                .expiresAt(LocalDateTime.now().plusMinutes(5)).used(false).build();
        User user = User.builder()
                .id(userId).fullName("Test").email("test@mail.escuelaing.edu.co")
                .password("old").verified(true).role(Role.ESTUDIANTE)
                .status(com.aibert.dosw.domain.model.user.UserStatus.ACTIVO).failedAttempts(0).build();

        when(tokenPort.findByToken("valid-token")).thenReturn(Optional.of(token));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bcryptValidator.encode(any())).thenReturn("newHashed");

        passwordResetService.resetPassword("valid-token", "NewPass1", "NewPass1");

        verify(userRepository).save(any());
        verify(tokenPort).save(any());
    }

    @Test
    void resetPassword_contrasenasNoCoinciden_lanzaException() {
        assertThrows(IllegalArgumentException.class,
                () -> passwordResetService.resetPassword("token", "Pass1", "Pass2"));
    }

    @Test
    void resetPassword_tokenNoExiste_lanzaInvalidCredentials() {
        when(tokenPort.findByToken(any())).thenReturn(Optional.empty());
        assertThrows(InvalidCredentialsException.class,
                () -> passwordResetService.resetPassword("bad-token", "Pass1", "Pass1"));
    }

    @Test
    void resetPassword_tokenUsado_lanzaInvalidCredentials() {
        when(tokenPort.findByToken(any())).thenReturn(
                Optional.of(buildToken(true, LocalDateTime.now().plusMinutes(5))));
        assertThrows(InvalidCredentialsException.class,
                () -> passwordResetService.resetPassword("used-token", "Pass1", "Pass1"));
    }

    @Test
    void resetPassword_tokenExpirado_lanzaInvalidCredentials() {
        when(tokenPort.findByToken(any())).thenReturn(
                Optional.of(buildToken(false, LocalDateTime.now().minusMinutes(1))));
        assertThrows(InvalidCredentialsException.class,
                () -> passwordResetService.resetPassword("expired-token", "Pass1", "Pass1"));
    }

    @Test
    void resetPassword_usuarioNoEncontrado_lanzaInvalidCredentials() {
        UUID userId = UUID.randomUUID();
        PasswordResetToken token = PasswordResetToken.builder()
                .id(1L).token("valid-token").userId(userId)
                .expiresAt(LocalDateTime.now().plusMinutes(5)).used(false).build();
        when(tokenPort.findByToken(any())).thenReturn(Optional.of(token));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(InvalidCredentialsException.class,
                () -> passwordResetService.resetPassword("valid-token", "Pass1", "Pass1"));
    }
}
