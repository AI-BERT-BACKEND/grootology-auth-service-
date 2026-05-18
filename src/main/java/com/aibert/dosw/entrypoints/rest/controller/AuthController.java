package com.aibert.dosw.entrypoints.rest.controller;

import com.aibert.dosw.application.dto.request.LoginRequestDTO;
import com.aibert.dosw.application.dto.response.LoginResponseDTO;
import com.aibert.dosw.application.dto.response.PasswordResetResponseDTO;
import com.aibert.dosw.application.service.TokenBlacklistService;
import com.aibert.dosw.domain.ports.in.LoginUseCase;
import com.aibert.dosw.domain.ports.in.PasswordResetUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user login, logout and password recovery")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final TokenBlacklistService tokenBlacklistService;
    private final PasswordResetUseCase passwordResetUseCase;

    @Operation(summary = "User login")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful."),
            @ApiResponse(responseCode = "400", description = "Missing or invalid fields."),
            @ApiResponse(responseCode = "401", description = "Incorrect email or password."),
            @ApiResponse(responseCode = "403", description = "Account is locked, inactive, or not verified.")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(loginUseCase.login(request));
    }

    @Operation(summary = "User logout")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Session closed successfully."),
            @ApiResponse(responseCode = "400", description = "Missing or malformed Authorization header.")
    })
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(
            @Parameter(description = "Bearer token to invalidate")
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            tokenBlacklistService.invalidate(authHeader.substring(7));
        }
        return ResponseEntity.ok(Map.of("sessionClosed", true, "redirectLogin", true));
    }

    @Operation(summary = "Request password reset")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reset email sent successfully."),
            @ApiResponse(responseCode = "404", description = "No account found with the provided email.")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<PasswordResetResponseDTO> forgotPassword(
            @Parameter(description = "Email address to reset the password for") @RequestParam String email) {
        return ResponseEntity.ok(passwordResetUseCase.requestPasswordReset(email));
    }

    @Operation(summary = "Reset password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password reset successfully."),
            @ApiResponse(responseCode = "400", description = "Passwords do not match."),
            @ApiResponse(responseCode = "401", description = "Token is invalid or has expired.")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Parameter(description = "Reset token received by email") @RequestParam String token,
            @Parameter(description = "New password") @RequestParam String newPassword,
            @Parameter(description = "New password confirmation") @RequestParam String confirmPassword) {
        passwordResetUseCase.resetPassword(token, newPassword, confirmPassword);
        return ResponseEntity.ok("Contraseña restablecida exitosamente.");
    }
}
