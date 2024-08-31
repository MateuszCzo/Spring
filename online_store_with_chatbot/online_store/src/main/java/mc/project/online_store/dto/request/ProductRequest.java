package mc.project.online_store.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductRequest {
    private long categoryId;
    private long manufacturerId;
    private MultipartFile cover;
    private String name;
    private String description;
    private String quantity;
    private String price;
    private boolean active;
    private List<MultipartFile> images;
    private List<Integer> attachmentsIds;
    private List<Integer> attributesIds;
}
