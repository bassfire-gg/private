package org.user_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.user_service.event.UserEventProducer;

@SpringBootTest
class UserServiceApplicationTests {

    @MockitoBean
    private UserEventProducer userEventProducer;

    @Test
    void contextLoads() {
    }

}
