package org.user_service.controller;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.user_service.assembler.UserModelAssembler;
import org.user_service.exception.ExceptionHandlers;
import org.user_service.mapper.UserMapper;

@WebMvcTest(UserController.class)
@Import({UserMapper.class, ExceptionHandlers.class, UserModelAssembler.class})
public class UserControllerTest {
}
