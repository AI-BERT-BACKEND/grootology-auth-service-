package com.aibert.dosw.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PasswordResetResponseDTO {
    private boolean recoveryStatus;
    private int expirationTime;
}
