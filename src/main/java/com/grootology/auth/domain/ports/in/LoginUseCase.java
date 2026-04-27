package com.grootology.auth.domain.ports.in;

import com.grootology.auth.application.dto.request.LoginRequestDTO;
import com.grootology.auth.application.dto.response.LoginResponseDTO;

public interface LoginUseCase {
    LoginResponseDTO login(LoginRequestDTO request);
}
