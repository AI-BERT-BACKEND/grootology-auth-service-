package com.aibert.dosw.entrypoints.advice;

import com.aibert.dosw.domain.exceptions.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleInvalidCredentials_retorna401() {
        ResponseEntity<Map<String, String>> r = handler.handleInvalidCredentials(new InvalidCredentialsException());
        assertEquals(HttpStatus.UNAUTHORIZED, r.getStatusCode());
        assertTrue(r.getBody().containsKey("error"));
    }

    @Test
    void handleNotVerified_retorna403() {
        ResponseEntity<Map<String, String>> r = handler.handleNotVerified(new AccountNotVerifiedException());
        assertEquals(HttpStatus.FORBIDDEN, r.getStatusCode());
    }

    @Test
    void handleLocked_retorna429() {
        ResponseEntity<Map<String, String>> r = handler.handleLocked(new AccountLockedException(5L));
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, r.getStatusCode());
    }

    @Test
    void handleInactive_retorna403() {
        ResponseEntity<Map<String, String>> r = handler.handleInactive(new AccountInactiveException());
        assertEquals(HttpStatus.FORBIDDEN, r.getStatusCode());
    }
}
