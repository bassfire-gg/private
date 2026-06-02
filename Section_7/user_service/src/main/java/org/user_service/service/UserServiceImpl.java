package org.user_service.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.user_service.entity.User;
import org.user_service.event.UserEvent;
import org.user_service.event.UserEventProducer;
import org.user_service.exception.UserNotFoundException;
import org.user_service.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserEventProducer  userEventProducer;

    @Override
    @Transactional
    public User createUser(User user) {
        User savedUser = userRepository.save(user);
        userEventProducer.sendUserEvent(new UserEvent(
                "CREATE",
                savedUser.getEmail(),
                savedUser.getId(),
                LocalDateTime.now()));
        return savedUser;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User updateUser(long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setAge(user.getAge());
        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        User  existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(existingUser);
        userEventProducer.sendUserEvent(new UserEvent(
                "DELETE",
                existingUser.getEmail(),
                existingUser.getId(),
                LocalDateTime.now()
        ));
    }
}
