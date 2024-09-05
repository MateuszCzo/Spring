package mc.project.online_store.controller.front;

import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.response.CategoryResponse;
import mc.project.online_store.dto.response.ImageResponse;
import mc.project.online_store.dto.response.ManufacturerResponse;
import mc.project.online_store.service.front.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageFrontController {
    private final ImageService imageService;

    @GetMapping("/category/{id}/image")
    public ResponseEntity<ImageResponse> getCategoryImage(
            @PathVariable(name = "id") long categoryId) {

        ImageResponse response = imageService.getCategoryImage(categoryId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/manufacturer/{id}/image")
    public ResponseEntity<ImageResponse> getManufacturerImage(
            @PathVariable(name = "id") long manufacturerId) {

        ImageResponse response = imageService.getManufacturerImage(manufacturerId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{id}/image")
    public ResponseEntity<ImageResponse> getProductImage(
            @PathVariable(name = "id") long productId) {

        ImageResponse response = imageService.getProductImage(productId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{id}/images")
    public ResponseEntity<List<ImageResponse>> getProductImages(
            @PathVariable(name = "id") long productId) {

        List<ImageResponse> responses = imageService.getProductImages(productId);

        return ResponseEntity.ok(responses);
    }
}