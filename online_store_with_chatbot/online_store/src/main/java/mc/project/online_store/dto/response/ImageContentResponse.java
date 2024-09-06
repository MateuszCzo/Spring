package mc.project.online_store.dto.response;

import lombok.Data;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

@Data
public class ImageContentResponse {
    private String fileName;
    private MediaType contentType;
    private Resource content;
}
