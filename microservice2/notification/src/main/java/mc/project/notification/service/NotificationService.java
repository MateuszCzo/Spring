package mc.project.notification.service;

import mc.project.notification.dto.NotificationRequest;

public interface NotificationService {
    void send(NotificationRequest notificationRequest);
}
