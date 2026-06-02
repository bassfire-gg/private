package org.user_service;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.user_service.controller.UserController;
import org.user_service.entity.User;
import org.user_service.exception.ExceptionHandlers;
import org.user_service.exception.UserNotFoundException;
import org.user_service.mapper.UserMapper;
import org.user_service.request.UserRequest;
import org.user_service.response.UserResponse;
import org.user_service.service.UserService;


import java.util.List;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import({UserMapper.class, ExceptionHandlers.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void getAllUsersThroughUserDto() throws Exception {
        when(userService.getAllUsers())
                .thenReturn(List.of(
                        buildUser(1L, "Jon", "jon@mail.ru", 30),
                        buildUser(2L, "Ivan", "ivan@mail.ru", 22)
                ));
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Jon"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Ivan"));
    }

    @Test
    void getUserByIdThroughUserDto() throws Exception {
        when(userService.getUserById(1L))
                .thenReturn(buildUser(1L, "Jon", "jon@mail.ru", 30));
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jon"))
                .andExpect(jsonPath("$.email").value("jon@mail.ru"))
                .andExpect(jsonPath("$.age").value(30));
    }

    @Test
    void createUserThroughUserDto() throws Exception {
        when(userService.createUser(any(User.class)))
                .thenReturn(buildUser(3L, "Kate", "kate@mail.ru", 18));
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildUserRequest("Kate", "kate@mail.ru", 18))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Kate"))
                .andExpect(jsonPath("$.email").value("kate@mail.ru"))
                .andExpect(jsonPath("$.age").value(18));

    }

    @Test
    void updateUserThroughUserDto() throws Exception {
        when(userService.updateUser(eq(1L), any(User.class)))
                .thenReturn(buildUser(1L, "Updated", "updated@mail.ru", 35));
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildUserRequest("Updated", "updated@mail.ru", 35))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.email").value("updated@mail.ru"))
                .andExpect(jsonPath("$.age").value(35));
    }

    @Test
    void deleteUsersReturnsOK() throws Exception {
        doNothing().when(userService).deleteUser(1L);
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());
        verify(userService).deleteUser(1L);
    }

    @Test
    void getUserByIdWhichReturnsNotFoundWhenUserDoesNotExist() throws Exception {
        doThrow(new UserNotFoundException(99L))
                .when(userService).getUserById(99L);
        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User with id 99 not found"));
    }


    private User buildUser(long id, String name, String email, int age) {
        User user = new User(name, email, age);
        user.setId(id);
        return user;
    }

    private UserRequest buildUserRequest(String name, String email, int age) {
        UserRequest userRequest = new UserRequest();
        userRequest.setName(name);
        userRequest.setEmail(email);
        userRequest.setAge(age);
        return userRequest;
    }
}
