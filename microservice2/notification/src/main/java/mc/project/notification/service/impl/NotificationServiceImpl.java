package mc.project.notification.service.impl;

import lombok.RequiredArgsConstructor;
import mc.project.notification.dto.NotificationRequest;
import mc.project.notification.model.Notification;
import mc.project.notification.repository.NotificationRepository;
import mc.project.notification.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    public void send(NotificationRequest notificationRequest) {
        Notification notification = Notification.builder()
                .toCustomerId(notificationRequest.toCustomerId())
                .toCustomerEmail(notificationRequest.toCustomerName())
                .sender("MC")
                .message(notificationRequest.message())
                .sendAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }
}
