package com.aibert.dosw.domain.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Correo o contraseña incorrectos");
    }
}
