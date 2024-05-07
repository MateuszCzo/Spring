package mc.project.notification.amqp.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.project.notification.amqp.AmqpNotificationConsumer;
import mc.project.notification.dto.NotificationRequest;
import mc.project.notification.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMQMessageConsumer implements AmqpNotificationConsumer {
    private final NotificationService notificationService;

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consumeString(String message) {
        log.info("Consuming message {}", message);
    }

    @RabbitListener(queues = {"${rabbitmq.json.queue.name}"})
    public void consumeNotificationRequest(NotificationRequest notificationRequest) {
        log.info("Consuming message {}", notificationRequest);
        notificationService.send(notificationRequest);
        log.info("Consumed message {}", notificationRequest);
    }
}
