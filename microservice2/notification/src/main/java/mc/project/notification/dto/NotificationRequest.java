package mc.project.notification.dto;

public record NotificationRequest(
        long toCustomerId,
        String toCustomerName,
        String message
) {}
