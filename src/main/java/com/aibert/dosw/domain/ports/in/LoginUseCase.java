package com.aibert.dosw.domain.ports.in;

import com.aibert.dosw.application.dto.request.LoginRequestDTO;
import com.aibert.dosw.application.dto.response.LoginResponseDTO;

public interface LoginUseCase {
    LoginResponseDTO login(LoginRequestDTO request);
}
