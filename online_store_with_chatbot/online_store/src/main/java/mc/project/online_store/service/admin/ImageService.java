package mc.project.online_store.service.admin;

import mc.project.online_store.dto.request.ImageRequest;
import mc.project.online_store.dto.response.ImageResponse;
import mc.project.online_store.model.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image postImage(MultipartFile file);

    Image putImage(Long id, MultipartFile file);

    void deleteImage(Image image);

    void deleteImage(long id);

    ImageResponse putImage(long id, ImageRequest request);

    ImageResponse postProductImage(long productId, ImageRequest request);

    void deleteProductImage(long productId, long id);
}
