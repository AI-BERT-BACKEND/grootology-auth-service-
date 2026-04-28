package com.aibert.dosw.domain.ports.out;

import com.aibert.dosw.domain.model.user.User;
import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByEmail(String email);
    void save(User user);
}
