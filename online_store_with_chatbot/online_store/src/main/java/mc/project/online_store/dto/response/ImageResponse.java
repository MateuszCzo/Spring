package mc.project.online_store.dto.response;

import lombok.Data;

@Data
public class ImageResponse {
    private Long id;
    private String name;
    private String path;
    private String type;
}
