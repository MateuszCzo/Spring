package mc.project.kafka.kafka.impl;

import lombok.extern.slf4j.Slf4j;
import mc.project.kafka.kafka.KafkaConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumerImpl implements KafkaConsumer {

    @KafkaListener(topics = "example-topic", groupId = "group-example")
    public void consume(String message) {
        log.info("Message received {}", message);
    }
}
