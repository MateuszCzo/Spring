package mc.project.customer.dto;

public record NotificationRequest(
        long toCustomerId,
        String toCustomerName,
        String message
) {}
