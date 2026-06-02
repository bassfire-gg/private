package org.notification_service;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.notification_service.event.UserEvent;
import org.notification_service.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        topics = {"user-events"},
        bootstrapServersProperty = "spring.kafka.bootstrap-servers"
)
class UserEventListenerIntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @MockitoBean
    private EmailService emailService;

    private KafkaTemplate<String, Object> kafkaTemplate;

    @BeforeEach
    void setUp() {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafka);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        producerProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        kafkaTemplate = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerProps));
    }

    @Test
    void testUserCreationEventProcessing() {
        UserEvent event = new UserEvent(
                "CREATE",
                "newuser@example.com",
                123L,
                LocalDateTime.now()
        );

        kafkaTemplate.send("user-events", String.valueOf(event.getUserId()), event).join();

        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> verify(emailService, times(1))
                        .sendUserCreationEmail("newuser@example.com"));
    }

    @Test
    void testUserDeletionEventProcessing() {
        UserEvent event = new UserEvent(
                "DELETE",
                "deleteuser@example.com",
                456L,
                LocalDateTime.now()
        );

        kafkaTemplate.send("user-events", String.valueOf(event.getUserId()), event).join();

        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> verify(emailService, times(1))
                        .sendUserDeletionEmail("deleteuser@example.com"));
    }
}
