package mc.project.kafka.service;

import mc.project.kafka.dto.UserRequest;

public interface KafkaService {
    void sendMessage(String message);

    void sendMessage(UserRequest userRequest);
}
