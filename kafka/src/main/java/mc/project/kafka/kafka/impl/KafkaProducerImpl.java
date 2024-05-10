package mc.project.kafka.kafka.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.project.kafka.dto.UserRequest;
import mc.project.kafka.kafka.KafkaProducer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerImpl implements KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTemplate<String, UserRequest> jsonKafkaTemplate;

    public void sendMessage(String message) {
        log.info("Message sent {}", message);
        kafkaTemplate.send("example-topic", message);
    }

    public void sendMessage(UserRequest userRequest) {
        log.info("User request sent {}", userRequest);

        Message<UserRequest> message = MessageBuilder
                .withPayload(userRequest)
                .setHeader(KafkaHeaders.TOPIC, "json-example-topic")
                .build();

        jsonKafkaTemplate.send(message);
    }
}
