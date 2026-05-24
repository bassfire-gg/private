package org.user_service.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.user_service.assembler.UserModelAssembler;
import org.user_service.entity.User;
import org.user_service.mapper.UserMapper;
import org.user_service.request.UserRequest;
import org.user_service.response.UserResponse;
import org.user_service.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Операции CRUD над пользователями")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserModelAssembler userModelAssembler;

    @Autowired
    public UserController(
            UserService userService,
            UserMapper userMapper,
            UserModelAssembler userModelAssembler
    ) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.userModelAssembler = userModelAssembler;
    }

    @GetMapping
    @Operation(summary = "Получить список пользователей", description = "Возвращает всех пользователей с HATEOAS-ссылками")
    @ApiResponse(responseCode = "200", description = "Список пользователей")
    public CollectionModel<EntityModel<UserResponse>> getUsers() {
        List<UserResponse> users = userService.getAllUsers().stream()
                .map(userMapper::toResponseDto)
                .toList();
        return userModelAssembler.toCollectionModel(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(schema = @Schema(example = "{\"message\":\"User with id 1 not found\"}")))
    })
    public EntityModel<UserResponse> getUser(@PathVariable long id) {
        return userModelAssembler.toModel(userMapper.toResponseDto(userService.getUserById(id)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    })
    public EntityModel<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);
        user.setCreated_at(LocalDateTime.now());
        return userModelAssembler.toModel(userMapper.toResponseDto(userService.createUser(user)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь обновлён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public EntityModel<UserResponse> updateUser(
            @PathVariable long id,
            @RequestBody UserRequest userRequest
    ) {
        User user = userMapper.toEntity(userRequest);
        return userModelAssembler.toModel(userMapper.toResponseDto(userService.updateUser(id, user)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Удалить пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь удалён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }
}
