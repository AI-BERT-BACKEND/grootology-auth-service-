package com.aibert.dosw.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(description = "Request body for user login")
public class LoginRequestDTO {

    @NotBlank
    @Email
    @Schema(description = "Registered email address of the user", example = "user@mail.escuelaing.edu.co")
    private String email;

    @NotBlank
    @Schema(description = "User's password", example = "MyPassword1")
    private String password;

    @Schema(description = "If true, extends the token expiration time", example = "false")
    private boolean rememberMe;
}
