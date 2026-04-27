package com.aibert.dosw.domain.exceptions;

public class AccountLockedException extends RuntimeException {
    public AccountLockedException(long minutesLeft) {
        super("Cuenta bloqueada temporalmente. Intenta en " + minutesLeft + " minuto(s)");
    }
}
