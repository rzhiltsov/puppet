package com.aston.puppet.mapper;

import com.aston.puppet.config.MapstructConfiguration;
import com.aston.puppet.dto.AddUserRequest;
import com.aston.puppet.dto.GetUserResponse;
import com.aston.puppet.dto.UpdateUserRequest;
import com.aston.puppet.entity.Requisites;
import com.aston.puppet.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapstructConfiguration.class)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requisites", expression = "java(toRequisites(addUserRequest))")
    User toUser(AddUserRequest addUserRequest);

    @Mapping(target = "id", ignore = true)
    Requisites toRequisites(AddUserRequest addUserRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requisites", expression = "java(toRequisites(user.getRequisites(), updateUserRequest))")
    void toUser(@MappingTarget User user, UpdateUserRequest updateUserRequest);

    @Mapping(target = "id", ignore = true)
    Requisites toRequisites(@MappingTarget Requisites requisites, UpdateUserRequest addUserRequest);

    @Mapping(target = "paymentAccount", source = "requisites.paymentAccount")
    @Mapping(target = "kbk", source = "requisites.kbk")
    GetUserResponse toGetUserResponse(User user);
}
