package mc.project.kafka.dto;

public record UserRequest(
        long id,
        String firstname,
        String lastname
) {
}
