package org.user_service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventProducer {
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Value("${app.kafka.user-events-topic}")
    private String userEventTopic;

    public void sendUserEvent(UserEvent event) {
        kafkaTemplate.send(userEventTopic, String.valueOf(event.getUserId()), event);
    }
}
