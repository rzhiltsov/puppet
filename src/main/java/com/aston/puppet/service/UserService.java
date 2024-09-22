package com.aston.puppet.service;

import com.aston.puppet.dto.AddUserRequest;
import com.aston.puppet.dto.GetUserResponse;
import com.aston.puppet.dto.UpdateUserRequest;
import com.aston.puppet.entity.User;
import com.aston.puppet.exception.AlreadyExistException;
import com.aston.puppet.exception.NoDataException;
import com.aston.puppet.exception.NotFoundException;
import com.aston.puppet.mapper.UserMapper;
import com.aston.puppet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private static final String USER_NOT_FOUND_MESSAGE = "Пользователь не найден";

    public UUID addUser(AddUserRequest addUserRequest) {
        User user = userMapper.toUser(addUserRequest);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            resolveConstraintViolationField(ex);
        }
        return user.getId();
    }

    public GetUserResponse getUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));
        return userMapper.toGetUserResponse(user);
    }

    public List<GetUserResponse> getAllUsers() {
        List<GetUserResponse> users = userRepository.findAll().stream().map(userMapper::toGetUserResponse).toList();
        if (users.isEmpty()) {
            throw new NoDataException();
        }
        return users;
    }

    public void updateUser(UUID id, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));
        userMapper.toUser(user, updateUserRequest);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            resolveConstraintViolationField(ex);
        }
    }

    public void deleteUser(UUID id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new NotFoundException(USER_NOT_FOUND_MESSAGE);
        }
    }

    private void resolveConstraintViolationField(DataIntegrityViolationException ex) {
        String errorMessage;
        Matcher matcher = Pattern.compile("\\((\\w+)\\)=").matcher(ex.getMessage());
        if (matcher.find()) {
            String field = matcher.group(1);
            errorMessage = "Нарушение уникальности поля " + field;
        } else {
            errorMessage = "Данный объект уже существует";
        }
        throw new AlreadyExistException(errorMessage);
    }
}
