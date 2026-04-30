package com.aibert.dosw.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class LoginResponseDTO {
    private UUID id;
    private String fullName;
    private String email;
    private String role;
    private String token;
}
