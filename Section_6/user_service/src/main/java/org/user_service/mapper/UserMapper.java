package org.user_service.mapper;

import org.springframework.stereotype.Component;
import org.user_service.entity.User;
import org.user_service.request.UserRequest;
import org.user_service.response.UserResponse;

@Component
public class UserMapper {
    public UserResponse toResponseDto(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreated_at()
        );
    }

    public User toEntity(UserRequest userRequest) {
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setAge(userRequest.getAge());
        return user;
    }
}
