package com.aibert.dosw.domain.exceptions;

public class AccountNotVerifiedException extends RuntimeException {
    public AccountNotVerifiedException() {
        super("La cuenta no ha sido verificada");
    }
}
