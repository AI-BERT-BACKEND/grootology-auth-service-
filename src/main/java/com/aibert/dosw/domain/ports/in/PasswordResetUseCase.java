package com.aibert.dosw.domain.ports.in;

import com.aibert.dosw.application.dto.response.PasswordResetResponseDTO;

public interface PasswordResetUseCase {
    PasswordResetResponseDTO requestPasswordReset(String email);
    void resetPassword(String token, String newPassword, String confirmPassword);
}
