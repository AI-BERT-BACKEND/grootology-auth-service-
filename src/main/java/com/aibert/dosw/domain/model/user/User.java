package com.aibert.dosw.domain.model.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {
    private Long id;
    private String email;
    private String password;
    private boolean verified;
    private int failedAttempts;
    private java.time.LocalDateTime lockedUntil;
}
