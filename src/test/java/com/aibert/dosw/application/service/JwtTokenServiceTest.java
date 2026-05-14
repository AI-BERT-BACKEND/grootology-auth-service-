package com.aibert.dosw.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {

    @InjectMocks private JwtTokenService jwtTokenService;

    private static final String SECRET = "wwEejxjgXFljx8rgl2axPocLobwRjRQnlgfeLLn9/24DNkFsiZgJweFc1ArLuydrraB3uhINOLGB1zJgRGWpRg==";

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenService, "secret", SECRET);
    }

    @Test
    void generateToken_sinRecordarme_generaTokenValido() {
        String token = jwtTokenService.generateToken("test@mail.escuelaing.edu.co", UUID.randomUUID(), "ESTUDIANTE", false);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void generateToken_conRecordarme_generaTokenValido() {
        String token = jwtTokenService.generateToken("test@mail.escuelaing.edu.co", UUID.randomUUID(), "ADMIN", true);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void generateToken_rememberMe_expiraMasTarde_queNormal() {
        String tokenNormal = jwtTokenService.generateToken("test@mail.escuelaing.edu.co", UUID.randomUUID(), "ESTUDIANTE", false);
        String tokenRemember = jwtTokenService.generateToken("test@mail.escuelaing.edu.co", UUID.randomUUID(), "ESTUDIANTE", true);
        assertNotNull(tokenNormal);
        assertNotNull(tokenRemember);
        // tokens distintos por diferente UUID y expiracion
        assertNotEquals(tokenNormal, tokenRemember);
    }
}
