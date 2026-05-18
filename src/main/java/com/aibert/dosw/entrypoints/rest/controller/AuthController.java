package com.aibert.dosw.entrypoints.rest.controller;

import com.aibert.dosw.application.dto.request.LoginRequestDTO;
import com.aibert.dosw.application.dto.response.LoginResponseDTO;
import com.aibert.dosw.application.service.TokenBlacklistService;
import com.aibert.dosw.domain.ports.in.LoginUseCase;
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
@Tag(name = "Authentication", description = "Endpoints for user login and logout")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final TokenBlacklistService tokenBlacklistService;

    @Operation(
            summary = "User login",
            description = "Validates the user's credentials and returns a signed JWT token along with basic user information. The token must be included in the Authorization header as 'Bearer <token>' for all protected endpoints."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful. Returns JWT token and user data."),
            @ApiResponse(responseCode = "400", description = "Missing or invalid fields in the request body."),
            @ApiResponse(responseCode = "401", description = "Incorrect email or password."),
            @ApiResponse(responseCode = "403", description = "Account is locked, inactive, or not verified.")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(loginUseCase.login(request));
    }

    @Operation(
            summary = "User logout",
            description = "Invalidates the provided JWT token by adding it to the blacklist. Even if the token has not expired, it will be rejected by the system after this call."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Session closed successfully."),
            @ApiResponse(responseCode = "400", description = "Missing or malformed Authorization header.")
    })
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(
            @Parameter(description = "Bearer token to invalidate. Format: Bearer <token>")
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            tokenBlacklistService.invalidate(authHeader.substring(7));
        }
        return ResponseEntity.ok(Map.of("sessionClosed", true, "redirectLogin", true));
    }
}
