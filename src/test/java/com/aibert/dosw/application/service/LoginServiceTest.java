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
import static org.mockito.ArgumentMatchers.eq;
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
                .profileComplete(true)
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
        assertNotNull(response.getId());
        assertNotNull(response.getFullName());
        assertNotNull(response.getEmail());
        assertTrue(response.isProfileComplete());
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

    @Test
    void login_conRememberMe_generaTokenLargo() {
        LoginRequestDTO request = mock(LoginRequestDTO.class);
        when(request.getEmail()).thenReturn("test@mail.escuelaing.edu.co");
        when(request.getPassword()).thenReturn("password123");
        when(request.isRememberMe()).thenReturn(true);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(buildUser(true, null)));
        when(bcryptValidator.matches(any(), any())).thenReturn(true);
        when(jwtTokenService.generateToken(any(), any(), any(), eq(true))).thenReturn("long-jwt-token");

        LoginResponseDTO response = loginService.login(request);
        assertNotNull(response.getToken());
    }

    @Test
    void login_intentosFallidosResetean_alLoginExitoso() {
        LoginRequestDTO request = buildRequest();
        User userWithAttempts = User.builder()
                .id(UUID.randomUUID())
                .fullName("Test")
                .email("test@mail.escuelaing.edu.co")
                .password("hashedPassword")
                .verified(true)
                .role(Role.ESTUDIANTE)
                .profileComplete(true)
                .failedAttempts(3)
                .build();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(userWithAttempts));
        when(bcryptValidator.matches(any(), any())).thenReturn(true);
        when(jwtTokenService.generateToken(any(), any(), any(), anyBoolean())).thenReturn("jwt-token");

        LoginResponseDTO response = loginService.login(request);
        assertNotNull(response.getToken());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void login_intentosFallidosAlcanzanMaximo_bloqueaCuenta() {
        LoginRequestDTO request = mock(LoginRequestDTO.class);
        when(request.getEmail()).thenReturn("test@mail.escuelaing.edu.co");
        when(request.getPassword()).thenReturn("wrongpass");
        User userWith4Attempts = User.builder()
                .id(UUID.randomUUID())
                .email("test@mail.escuelaing.edu.co")
                .password("hashedPassword")
                .verified(true)
                .role(Role.ESTUDIANTE)
                .failedAttempts(4)
                .build();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(userWith4Attempts));
        when(bcryptValidator.matches(any(), any())).thenReturn(false);
        assertThrows(InvalidCredentialsException.class, () -> loginService.login(request));
        verify(userRepository).save(any());
    }

    @Test
    void login_cuentaInactiva_lanzaAccountInactive() {
        LoginRequestDTO request = mock(LoginRequestDTO.class);
        when(request.getEmail()).thenReturn("test@mail.escuelaing.edu.co");
        when(request.getPassword()).thenReturn("password123");
        User inactiveUser = User.builder()
                .id(UUID.randomUUID())
                .email("test@mail.escuelaing.edu.co")
                .password("hashedPassword")
                .verified(true)
                .role(Role.ESTUDIANTE)
                .status("INACTIVO")
                .failedAttempts(0)
                .build();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(inactiveUser));
        when(bcryptValidator.matches(any(), any())).thenReturn(true);
        assertThrows(com.aibert.dosw.domain.exceptions.AccountInactiveException.class,
                () -> loginService.login(request));
    }

    @Test
    void login_cuentaBloqueadaExpirada_permiteLogin() {
        LoginRequestDTO request = buildRequest();
        User userWithExpiredLock = User.builder()
                .id(UUID.randomUUID())
                .fullName("Test")
                .email("test@mail.escuelaing.edu.co")
                .password("hashedPassword")
                .verified(true)
                .role(Role.ESTUDIANTE)
                .profileComplete(true)
                .failedAttempts(0)
                .lockedUntil(LocalDateTime.now().minusMinutes(1))
                .build();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(userWithExpiredLock));
        when(bcryptValidator.matches(any(), any())).thenReturn(true);
        when(jwtTokenService.generateToken(any(), any(), any(), anyBoolean())).thenReturn("jwt-token");

        LoginResponseDTO response = loginService.login(request);
        assertNotNull(response.getToken());
    }

    @Test
    void loginResponseDTO_camposCorrectos() {
        LoginResponseDTO dto = LoginResponseDTO.builder()
                .id(UUID.randomUUID())
                .fullName("Test User")
                .email("test@mail.escuelaing.edu.co")
                .role("ESTUDIANTE")
                .token("jwt-token")
                .profileComplete(true)
                .build();
        assertNotNull(dto.getId());
        assertEquals("Test User", dto.getFullName());
        assertEquals("test@mail.escuelaing.edu.co", dto.getEmail());
        assertEquals("ESTUDIANTE", dto.getRole());
        assertEquals("jwt-token", dto.getToken());
        assertTrue(dto.isProfileComplete());
    }
}
