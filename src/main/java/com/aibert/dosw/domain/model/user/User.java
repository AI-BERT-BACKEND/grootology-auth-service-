package com.aibert.dosw.domain.model.user;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class User {
    private UUID id;
    private String fullName;
    private String email;
    private String password;
    private boolean verified;
    private Role role;
    private UserStatus status;
    private boolean profileComplete;
    private Integer failedAttempts;
    private java.time.LocalDateTime lockedUntil;
}
