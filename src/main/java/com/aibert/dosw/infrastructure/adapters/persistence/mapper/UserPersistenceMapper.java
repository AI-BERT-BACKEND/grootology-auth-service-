package com.aibert.dosw.infrastructure.adapters.persistence.mapper;

import com.aibert.dosw.domain.model.user.User;
import com.aibert.dosw.infrastructure.adapters.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {
    User toDomain(UserEntity entity);
    UserEntity toEntity(User user);
}
