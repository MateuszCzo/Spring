package mc.project.kafka.kafka;

import mc.project.kafka.dto.UserRequest;

public interface KafkaProducer {
    void sendMessage(String message);

    void sendMessage(UserRequest userRequest);
}
