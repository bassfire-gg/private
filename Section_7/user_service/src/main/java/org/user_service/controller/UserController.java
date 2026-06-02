package org.user_service.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.user_service.entity.User;
import org.user_service.mapper.UserMapper;
import org.user_service.request.UserRequest;
import org.user_service.response.UserResponse;
import org.user_service.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public List<UserResponse> getUsers() {
        return userService.getAllUsers().stream().map(userMapper::toResponseDto).toList();
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable long id) {
        return userMapper.toResponseDto(userService.getUserById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);
        user.setCreated_at(LocalDateTime.now());
        return userMapper.toResponseDto(userService.createUser(user));
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable long id,
            @RequestBody UserRequest userRequest
    ) {
        User user = userMapper.toEntity(userRequest);
        return userMapper.toResponseDto(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }
}
