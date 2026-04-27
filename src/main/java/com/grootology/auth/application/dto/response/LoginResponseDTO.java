package com.grootology.auth.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDTO {
    private String token;
}
