package mc.project.online_store.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AttachmentRequest {
    @NotNull(message = "Username cannot be empty")
    private String name;
    @NotNull(message = "Username cannot be empty")
    private String description;
    @NotNull(message = "Username cannot be empty")
    private MultipartFile file;
}
