package com.aibert.dosw.domain.exceptions;

public class AccountInactiveException extends RuntimeException {
    public AccountInactiveException() {
        super("Tu cuenta está desactivada. Contacta al administrador.");
    }
}
