package com.grootology.auth.infrastructure.adapters.persistence.mapper;

import com.grootology.auth.domain.model.user.User;
import com.grootology.auth.infrastructure.adapters.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {
    User toDomain(UserEntity entity);
    UserEntity toEntity(User user);
}
