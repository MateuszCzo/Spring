package mc.project.notification.amqp;

import mc.project.notification.dto.NotificationRequest;

public interface AmqpNotificationConsumer {
    void consumeString(String message);

    void consumeNotificationRequest(NotificationRequest notificationRequest);
}
