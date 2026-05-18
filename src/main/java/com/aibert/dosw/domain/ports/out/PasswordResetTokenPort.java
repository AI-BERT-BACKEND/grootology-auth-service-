package com.aibert.dosw.domain.ports.out;

import com.aibert.dosw.domain.model.user.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenPort {
    PasswordResetToken save(PasswordResetToken token);
    Optional<PasswordResetToken> findByToken(String token);
}
