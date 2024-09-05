package mc.project.online_store.service.front;

import mc.project.online_store.dto.response.ImageResponse;

import java.util.List;

public interface ImageService {
    ImageResponse getCategoryImage(long categoryId);

    ImageResponse getManufacturerImage(long manufacturerId);

    ImageResponse getProductImage(long productId);

    List<ImageResponse> getProductImages(long productId);
}
