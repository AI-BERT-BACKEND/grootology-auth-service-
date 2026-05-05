package com.aibert.dosw.application.service;

import com.aibert.dosw.application.dto.request.LoginRequestDTO;
import com.aibert.dosw.application.dto.response.LoginResponseDTO;
import com.aibert.dosw.domain.exceptions.AccountLockedException;
import com.aibert.dosw.domain.exceptions.AccountNotVerifiedException;
import com.aibert.dosw.domain.exceptions.InvalidCredentialsException;
import com.aibert.dosw.domain.model.user.Role;
import com.aibert.dosw.domain.model.user.User;
import com.aibert.dosw.domain.ports.out.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock private UserRepositoryPort userRepository;
    @Mock private JwtTokenService jwtTokenService;
    @Mock private BCryptPasswordEncoder bcryptValidator;

    @InjectMocks private LoginService loginService;

    private User buildUser(boolean verified, LocalDateTime lockedUntil) {
        return User.builder()
                .id(UUID.randomUUID())
                .fullName("Test User")
                .email("test@mail.escuelaing.edu.co")
                .password("hashedPassword")
                .verified(verified)
                .role(Role.ESTUDIANTE)
                .failedAttempts(0)
                .lockedUntil(lockedUntil)
                .build();
    }

    private LoginRequestDTO buildRequest() {
        LoginRequestDTO request = mock(LoginRequestDTO.class);
        when(request.getEmail()).thenReturn("test@mail.escuelaing.edu.co");
        when(request.getPassword()).thenReturn("password123");
        when(request.isRememberMe()).thenReturn(false);
        return request;
    }

    @Test
    void login_credencialesCorrectas_retornaToken() {
        LoginRequestDTO request = buildRequest();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(buildUser(true, null)));
        when(bcryptValidator.matches(any(), any())).thenReturn(true);
        when(jwtTokenService.generateToken(any(), any(), any(), anyBoolean())).thenReturn("jwt-token");

        LoginResponseDTO response = loginService.login(request);

        assertNotNull(response.getToken());
        assertEquals("ESTUDIANTE", response.getRole());
    }

    @Test
    void login_usuarioNoExiste_lanzaInvalidCredentials() {
        LoginRequestDTO request = mock(LoginRequestDTO.class);
        when(request.getEmail()).thenReturn("noexiste@mail.escuelaing.edu.co");
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        assertThrows(InvalidCredentialsException.class, () -> loginService.login(request));
    }

    @Test
    void login_contrasenaIncorrecta_lanzaInvalidCredentials() {
        LoginRequestDTO request = mock(LoginRequestDTO.class);
        when(request.getEmail()).thenReturn("test@mail.escuelaing.edu.co");
        when(request.getPassword()).thenReturn("wrongpass");
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(buildUser(true, null)));
        when(bcryptValidator.matches(any(), any())).thenReturn(false);
        assertThrows(InvalidCredentialsException.class, () -> loginService.login(request));
    }

    @Test
    void login_cuentaNoVerificada_lanzaAccountNotVerified() {
        LoginRequestDTO request = mock(LoginRequestDTO.class);
        when(request.getEmail()).thenReturn("test@mail.escuelaing.edu.co");
        when(request.getPassword()).thenReturn("password123");
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(buildUser(false, null)));
        when(bcryptValidator.matches(any(), any())).thenReturn(true);
        assertThrows(AccountNotVerifiedException.class, () -> loginService.login(request));
    }

    @Test
    void login_cuentaBloqueada_lanzaAccountLocked() {
        LoginRequestDTO request = mock(LoginRequestDTO.class);
        when(request.getEmail()).thenReturn("test@mail.escuelaing.edu.co");
        when(userRepository.findByEmail(any())).thenReturn(
                Optional.of(buildUser(true, LocalDateTime.now().plusMinutes(10))));
        assertThrows(AccountLockedException.class, () -> loginService.login(request));
    }
}
