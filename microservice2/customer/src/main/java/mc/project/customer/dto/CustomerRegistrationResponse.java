package mc.project.customer.dto;

public record CustomerRegistrationResponse(
        long id,
        String firstname,
        String lastname,
        String email
) {}