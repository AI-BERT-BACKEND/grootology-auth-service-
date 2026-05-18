package com.aibert.dosw.infrastructure.adapters.persistence.mapper;

import com.aibert.dosw.domain.model.user.PasswordResetToken;
import com.aibert.dosw.infrastructure.adapters.persistence.entity.PasswordResetTokenEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PasswordResetTokenMapper {
    PasswordResetToken toDomain(PasswordResetTokenEntity entity);
    PasswordResetTokenEntity toEntity(PasswordResetToken token);
}
