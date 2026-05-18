package com.aibert.dosw.domain.model.user;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class PasswordResetToken {
    private Long id;
    private String token;
    private UUID userId;
    private LocalDateTime expiresAt;
    private boolean used;
}
