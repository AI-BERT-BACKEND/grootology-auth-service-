package com.aibert.dosw.application.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenBlacklistServiceTest {

    private final TokenBlacklistService service = new TokenBlacklistService();

    @Test
    void invalidate_tokenNuevo_agregaABlacklist() {
        service.invalidate("token123");
        assertTrue(service.isBlacklisted("token123"));
    }

    @Test
    void isBlacklisted_tokenNoInvalidado_retornaFalse() {
        assertFalse(service.isBlacklisted("tokenNoExiste"));
    }

    @Test
    void invalidate_mismoTokenDosVeces_noFalla() {
        service.invalidate("token123");
        service.invalidate("token123");
        assertTrue(service.isBlacklisted("token123"));
    }
}
