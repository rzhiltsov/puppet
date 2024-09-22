package com.aston.puppet.mapper;

import com.aston.puppet.config.MapstructConfiguration;
import com.aston.puppet.dto.AddUserRequest;
import com.aston.puppet.dto.GetUserResponse;
import com.aston.puppet.dto.UpdateUserRequest;
import com.aston.puppet.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapstructConfiguration.class)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toUser(AddUserRequest addUserRequest);

    @Mapping(target = "id", ignore = true)
    void toUser(@MappingTarget User user, UpdateUserRequest updateUserRequest);

    GetUserResponse toGetUserResponse(User user);
}
