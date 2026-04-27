package com.grootology.auth.domain.exceptions;

public class AccountNotVerifiedException extends RuntimeException {
    public AccountNotVerifiedException() {
        super("La cuenta no ha sido verificada");
    }
}
