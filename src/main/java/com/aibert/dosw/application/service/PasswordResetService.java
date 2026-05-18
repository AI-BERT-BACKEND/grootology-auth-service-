package com.aibert.dosw.application.service;

import com.aibert.dosw.application.dto.response.PasswordResetResponseDTO;
import com.aibert.dosw.domain.exceptions.InvalidCredentialsException;
import com.aibert.dosw.domain.model.user.PasswordResetToken;
import com.aibert.dosw.domain.model.user.User;
import com.aibert.dosw.domain.ports.in.PasswordResetUseCase;
import com.aibert.dosw.domain.ports.out.EmailServicePort;
import com.aibert.dosw.domain.ports.out.PasswordResetTokenPort;
import com.aibert.dosw.domain.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService implements PasswordResetUseCase {

    private static final int EXPIRY_MINUTES = 5;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final UserRepositoryPort userRepository;
    private final PasswordResetTokenPort tokenPort;
    private final EmailServicePort emailService;
    private final BCryptPasswordEncoder bcryptValidator;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public PasswordResetResponseDTO requestPasswordReset(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            String token = UUID.randomUUID().toString();
            tokenPort.save(PasswordResetToken.builder()
                    .token(token)
                    .userId(user.getId())
                    .expiresAt(LocalDateTime.now().plusMinutes(EXPIRY_MINUTES))
                    .used(false)
                    .build());
            emailService.sendRecoveryEmail(email, baseUrl + "/api/auth/reset-password?token=" + token);
        });

        return PasswordResetResponseDTO.builder()
                .recoveryStatus(true)
                .expirationTime(EXPIRY_MINUTES)
                .build();
    }

    @Override
    public void resetPassword(String token, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        PasswordResetToken resetToken = tokenPort.findByToken(token)
                .orElseThrow(InvalidCredentialsException::new);

        if (resetToken.isUsed() || resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidCredentialsException();
        }

        User user = userRepository.findById(resetToken.getUserId())
                .orElseThrow(InvalidCredentialsException::new);

        userRepository.save(User.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .password(bcryptValidator.encode(newPassword))
                .verified(user.isVerified())
                .role(user.getRole())
                .status(user.getStatus())
                .failedAttempts(0)
                .lockedUntil(null)
                .build());

        tokenPort.save(PasswordResetToken.builder()
                .id(resetToken.getId())
                .token(resetToken.getToken())
                .userId(resetToken.getUserId())
                .expiresAt(resetToken.getExpiresAt())
                .used(true)
                .build());
    }
}
