package com.aibert.dosw.application.service;

import com.aibert.dosw.application.dto.request.LoginRequestDTO;
import com.aibert.dosw.application.dto.response.LoginResponseDTO;
import com.aibert.dosw.domain.exceptions.AccountLockedException;
import com.aibert.dosw.domain.exceptions.AccountNotVerifiedException;
import com.aibert.dosw.domain.exceptions.InvalidCredentialsException;
import com.aibert.dosw.domain.model.user.User;
import com.aibert.dosw.domain.ports.in.LoginUseCase;
import com.aibert.dosw.domain.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

    private static final int MAX_ATTEMPTS = 5;
    private static final int LOCK_MINUTES = 15;

    private final UserRepositoryPort userRepository;
    private final JwtTokenService jwtTokenService;
    private final BCryptPasswordEncoder bcryptValidator;

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        checkLock(user);

        if (!bcryptValidator.matches(request.getPassword(), user.getPassword())) {
            handleFailedAttempt(user);
            throw new InvalidCredentialsException();
        }

        if (!user.isVerified()) {
            throw new AccountNotVerifiedException();
        }

        resetFailedAttempts(user);

        String token = jwtTokenService.generateToken(
                user.getEmail(), user.getId(), user.getRole().name(), request.isRememberMe());

        return LoginResponseDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .token(token)
                .build();
    }

    private void checkLock(User user) {
        if (user.getLockedUntil() != null && LocalDateTime.now().isBefore(user.getLockedUntil())) {
            long minutesLeft = ChronoUnit.MINUTES.between(LocalDateTime.now(), user.getLockedUntil()) + 1;
            throw new AccountLockedException(minutesLeft);
        }
    }

    private void handleFailedAttempt(User user) {
        int attempts = (user.getFailedAttempts() == null ? 0 : user.getFailedAttempts()) + 1;
        LocalDateTime lockedUntil = attempts >= MAX_ATTEMPTS
                ? LocalDateTime.now().plusMinutes(LOCK_MINUTES)
                : user.getLockedUntil();

        userRepository.save(User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .verified(user.isVerified())
                .role(user.getRole())
                .failedAttempts(attempts >= MAX_ATTEMPTS ? 0 : attempts)
                .lockedUntil(lockedUntil)
                .build());
    }

    private void resetFailedAttempts(User user) {
        if (user.getFailedAttempts() != null && user.getFailedAttempts() > 0 || user.getLockedUntil() != null) {
            userRepository.save(User.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .verified(user.isVerified())
                    .role(user.getRole())
                    .failedAttempts(0)
                    .lockedUntil(null)
                    .build());
        }
    }
}
