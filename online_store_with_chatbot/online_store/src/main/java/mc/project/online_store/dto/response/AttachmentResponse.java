package mc.project.online_store.dto.response;

import lombok.Data;

@Data
public class AttachmentResponse {
    private Long id;
    private String name;
    private String description;
    private String extension;
}
