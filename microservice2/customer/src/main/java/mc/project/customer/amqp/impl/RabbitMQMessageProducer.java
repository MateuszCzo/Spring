package mc.project.customer.amqp.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.project.customer.amqp.AmqpNotificationProducer;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitMQMessageProducer implements AmqpNotificationProducer {
    private final AmqpTemplate amqpTemplate;

    public void publish(Object payload, String exchange, String routingKey) {
        log.info("Publishing to {} using routingKye {}. Payload {}.", exchange, routingKey, payload);
        amqpTemplate.convertAndSend(exchange, routingKey, payload);
        log.info("Published to {} using routingKye {}. Payload {}.", exchange, routingKey, payload);
    }
}
