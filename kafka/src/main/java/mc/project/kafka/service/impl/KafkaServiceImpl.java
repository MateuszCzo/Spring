package mc.project.kafka.service.impl;

import lombok.RequiredArgsConstructor;
import mc.project.kafka.dto.UserRequest;
import mc.project.kafka.kafka.KafkaProducer;
import mc.project.kafka.service.KafkaService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {
    private final KafkaProducer kafkaProducer;

    public void sendMessage(String message) {
        kafkaProducer.sendMessage(message);
    }

    public void sendMessage(UserRequest userRequest) {
        kafkaProducer.sendMessage(userRequest);
    }
}
