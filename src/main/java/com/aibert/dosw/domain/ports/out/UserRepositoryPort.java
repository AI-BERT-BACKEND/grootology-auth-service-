package com.aibert.dosw.domain.ports.out;

import com.aibert.dosw.domain.model.user.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
    void save(User user);
}
