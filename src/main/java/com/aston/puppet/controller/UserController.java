package com.aston.puppet.controller;

import com.aston.puppet.dto.AddUserRequest;
import com.aston.puppet.dto.GetUserResponse;
import com.aston.puppet.dto.UpdateUserRequest;
import com.aston.puppet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> addUser(@RequestBody AddUserRequest addUserRequest) {
        UUID id = userService.addUser(addUserRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/users/".concat(id.toString()))
                .build();
    }

    @GetMapping("/{id}")
    public GetUserResponse getUser(@PathVariable UUID id) {
        return userService.getUser(id);
    }

    @GetMapping
    public List<GetUserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PatchMapping("/{id}")
    public void updateUser(@PathVariable UUID id, @RequestBody UpdateUserRequest updateUserRequest) {
        userService.updateUser(id, updateUserRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }
}
