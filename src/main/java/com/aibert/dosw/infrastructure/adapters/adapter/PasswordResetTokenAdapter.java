package com.aibert.dosw.infrastructure.adapters.adapter;

import com.aibert.dosw.domain.model.user.PasswordResetToken;
import com.aibert.dosw.domain.ports.out.PasswordResetTokenPort;
import com.aibert.dosw.infrastructure.adapters.persistence.mapper.PasswordResetTokenMapper;
import com.aibert.dosw.infrastructure.adapters.persistence.repository.PasswordResetTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PasswordResetTokenAdapter implements PasswordResetTokenPort {

    private final PasswordResetTokenJpaRepository jpaRepository;
    private final PasswordResetTokenMapper mapper;

    @Override
    public PasswordResetToken save(PasswordResetToken token) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(token)));
    }

    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        return jpaRepository.findByToken(token).map(mapper::toDomain);
    }
}
