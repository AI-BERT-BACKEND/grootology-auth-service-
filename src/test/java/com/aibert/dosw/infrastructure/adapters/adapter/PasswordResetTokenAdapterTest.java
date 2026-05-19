package com.aibert.dosw.infrastructure.adapters.adapter;

import com.aibert.dosw.domain.model.user.PasswordResetToken;
import com.aibert.dosw.infrastructure.adapters.persistence.entity.PasswordResetTokenEntity;
import com.aibert.dosw.infrastructure.adapters.persistence.mapper.PasswordResetTokenMapper;
import com.aibert.dosw.infrastructure.adapters.persistence.repository.PasswordResetTokenJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetTokenAdapterTest {

    @Mock private PasswordResetTokenJpaRepository jpaRepository;
    @Mock private PasswordResetTokenMapper mapper;
    @InjectMocks private PasswordResetTokenAdapter adapter;

    private PasswordResetToken buildDomain() {
        return PasswordResetToken.builder()
                .id(1L).token("token").userId(UUID.randomUUID())
                .expiresAt(LocalDateTime.now().plusMinutes(5)).used(false).build();
    }

    private PasswordResetTokenEntity buildEntity() {
        return PasswordResetTokenEntity.builder()
                .id(1L).token("token").userId(UUID.randomUUID())
                .expiresAt(LocalDateTime.now().plusMinutes(5)).used(false).build();
    }

    @Test
    void save_guardaYRetornaDomain() {
        PasswordResetToken domain = buildDomain();
        PasswordResetTokenEntity entity = buildEntity();
        when(mapper.toEntity(domain)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        PasswordResetToken result = adapter.save(domain);

        assertNotNull(result);
        assertEquals("token", result.getToken());
    }

    @Test
    void findByToken_existente_retornaOptional() {
        PasswordResetTokenEntity entity = buildEntity();
        PasswordResetToken domain = buildDomain();
        when(jpaRepository.findByToken("token")).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<PasswordResetToken> result = adapter.findByToken("token");

        assertTrue(result.isPresent());
        assertEquals("token", result.get().getToken());
    }

    @Test
    void findByToken_noExistente_retornaEmpty() {
        when(jpaRepository.findByToken(any())).thenReturn(Optional.empty());
        Optional<PasswordResetToken> result = adapter.findByToken("noexiste");
        assertTrue(result.isEmpty());
    }
}
