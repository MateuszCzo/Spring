package mc.project.kafka.kafka;

import mc.project.kafka.dto.UserRequest;

public interface KafkaConsumer {
    void consume(String message);

    void consume(UserRequest userRequest);
}
