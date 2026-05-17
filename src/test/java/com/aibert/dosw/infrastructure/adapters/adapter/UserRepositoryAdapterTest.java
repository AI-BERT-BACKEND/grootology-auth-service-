package com.aibert.dosw.infrastructure.adapters.adapter;

import com.aibert.dosw.domain.model.user.Role;
import com.aibert.dosw.domain.model.user.User;
import com.aibert.dosw.domain.model.user.UserStatus;
import com.aibert.dosw.infrastructure.adapters.persistence.entity.UserEntity;
import com.aibert.dosw.infrastructure.adapters.persistence.mapper.UserPersistenceMapper;
import com.aibert.dosw.infrastructure.adapters.persistence.repository.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @Mock private UserJpaRepository jpaRepository;
    @Mock private UserPersistenceMapper mapper;
    @InjectMocks private UserRepositoryAdapter adapter;

    private final UUID userId = UUID.randomUUID();

    private UserEntity buildEntity() {
        UserEntity e = new UserEntity();
        e.setId(userId);
        e.setEmail("test@mail.escuelaing.edu.co");
        e.setFullName("Test");
        e.setPassword("hashed");
        e.setVerified(true);
        e.setRole(Role.ESTUDIANTE);
        e.setStatus(UserStatus.ACTIVO);
        return e;
    }

    private User buildUser() {
        return User.builder().id(userId).email("test@mail.escuelaing.edu.co")
                .fullName("Test").password("hashed").verified(true)
                .role(Role.ESTUDIANTE).status(UserStatus.ACTIVO).build();
    }

    @Test
    void findByEmail_existe_retornaUsuario() {
        when(jpaRepository.findByEmail("test@mail.escuelaing.edu.co")).thenReturn(Optional.of(buildEntity()));
        when(mapper.toDomain(any())).thenReturn(buildUser());
        Optional<User> result = adapter.findByEmail("test@mail.escuelaing.edu.co");
        assertTrue(result.isPresent());
    }

    @Test
    void findByEmail_noExiste_retornaVacio() {
        when(jpaRepository.findByEmail(any())).thenReturn(Optional.empty());
        assertTrue(adapter.findByEmail("noexiste@mail.escuelaing.edu.co").isEmpty());
    }

    @Test
    void findById_existe_retornaUsuario() {
        when(jpaRepository.findById(userId)).thenReturn(Optional.of(buildEntity()));
        when(mapper.toDomain(any())).thenReturn(buildUser());
        Optional<User> result = adapter.findById(userId);
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
    }

    @Test
    void findById_noExiste_retornaVacio() {
        when(jpaRepository.findById(any())).thenReturn(Optional.empty());
        assertTrue(adapter.findById(UUID.randomUUID()).isEmpty());
    }

    @Test
    void save_llamaJpaRepository() {
        when(mapper.toEntity(any())).thenReturn(buildEntity());
        adapter.save(buildUser());
        verify(jpaRepository).save(any());
    }
}
