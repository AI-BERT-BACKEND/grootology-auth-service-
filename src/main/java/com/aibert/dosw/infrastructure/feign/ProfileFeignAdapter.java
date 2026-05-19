package com.aibert.dosw.infrastructure.feign;

import com.aibert.dosw.application.dto.request.UserAuthUpdateDTO;
import com.aibert.dosw.application.dto.response.UserAuthDTO;
import com.aibert.dosw.domain.model.user.User;
import com.aibert.dosw.domain.ports.out.UserRepositoryPort;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@Primary
@RequiredArgsConstructor
public class ProfileFeignAdapter implements UserRepositoryPort {

    private final ProfileFeignClient feignClient;

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            return Optional.of(toUser(feignClient.findByEmail(email)));
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findById(UUID id) {
        try {
            return Optional.of(toUser(feignClient.findById(id)));
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(User user) {
        feignClient.updateAuthFields(user.getId(),
                new UserAuthUpdateDTO(user.getFailedAttempts(), user.getLockedUntil()));
    }

    private User toUser(UserAuthDTO dto) {
        return User.builder()
                .id(dto.getId())
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .verified(dto.isVerified())
                .role(dto.getRole())
                .status(dto.getStatus())
                .profileComplete(dto.isProfileComplete())
                .failedAttempts(dto.getFailedAttempts())
                .lockedUntil(dto.getLockedUntil())
                .build();
    }
}
