package com.aibert.dosw.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserAuthUpdateDTO {
    private Integer failedAttempts;
    private LocalDateTime lockedUntil;
}
