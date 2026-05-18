package com.aibert.dosw.domain.ports.out;

public interface EmailServicePort {
    void sendVerificationEmail(String toEmail, String verificationLink);
    void sendRecoveryEmail(String toEmail, String recoveryLink);
}
