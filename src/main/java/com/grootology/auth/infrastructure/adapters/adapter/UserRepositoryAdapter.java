package com.grootology.auth.infrastructure.adapters.adapter;

import com.grootology.auth.domain.model.user.User;
import com.grootology.auth.domain.ports.out.UserRepositoryPort;
import com.grootology.auth.infrastructure.adapters.persistence.mapper.UserPersistenceMapper;
import com.grootology.auth.infrastructure.adapters.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository jpaRepository;
    private final UserPersistenceMapper mapper;

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public void save(User user) {
        jpaRepository.save(mapper.toEntity(user));
    }
}
