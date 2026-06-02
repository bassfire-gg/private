package org.user_service.service;

import org.user_service.entity.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(long id);
    List<User> getAllUsers();
    User updateUser(long id, User user);
    void deleteUser(long id);
}
