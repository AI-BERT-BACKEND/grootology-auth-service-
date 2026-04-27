package com.grootology.auth.domain.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Correo o contraseña incorrectos");
    }
}
