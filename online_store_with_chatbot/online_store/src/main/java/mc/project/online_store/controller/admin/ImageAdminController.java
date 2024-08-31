package mc.project.online_store.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.ImageRequest;
import mc.project.online_store.dto.response.ImageResponse;
import mc.project.online_store.service.admin.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ImageAdminController {
    private final ImageService imageService;

    @PutMapping("/image/{id}")
    public ResponseEntity<ImageResponse> putImage(
            @PathVariable(name = "id") long id,
            @Valid @RequestBody ImageRequest request) {

        ImageResponse response = imageService.putImage(id, request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/product/{id}/image")
    public ResponseEntity<ImageResponse> postProductImage(
            @PathVariable(name = "id") long productId,
            @Valid @RequestBody ImageRequest request) {

        ImageResponse response = imageService.postProductImage(productId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/product/{productId}/image/{id}")
    public void deleteProductImage(
            @PathVariable(name = "productId") long productId,
            @PathVariable(name = "id") long id) {

        imageService.deleteProductImage(productId, id);
    }
}
